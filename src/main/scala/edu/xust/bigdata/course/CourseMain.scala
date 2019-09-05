package edu.xust.bigdata.course

import edu.xust.bigdata.utils.{IPUtil, JedisUtil}
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis


object CourseMain {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("CourseMain")
    conf.setMaster("local[2]")

    val ssc = new StreamingContext(conf, Seconds(3))
    val sc = ssc.sparkContext
    val ipsRDD: RDD[String] = sc.textFile("D:\\resource\\ip.txt")
    val ipFileRDD: RDD[Array[String]] = ipsRDD.map(_.split('|'))
    val t3RDD: RDD[(String, String, String)] = ipFileRDD.map(arry => (arry(2), arry(3), arry(4) + "-" + arry(5) + "-" + arry(6) + "-" + arry(7)))
    val broadcast: Broadcast[Array[(String, String, String)]] = sc.broadcast(t3RDD.collect())
    val inDS: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, Map("metadata.broker.list" -> "node3:9092,node4:9092,node5:9092"), Set("hadoop"))
    val lineDS: DStream[String] = inDS.map(_._2)
    val t6DS: DStream[(String, String, String, String, String, String)] = lineDS.map(line => line.split(',')).map(ele => (ele(0).split(' ')(0), ele(1), ele(2).split('/')(2), ele(2).split('/')(4), ele(2).split('/')(5), ele(2).split('/')(6).split('.')(0)))
    t6DS.foreachRDD(rdd => {
      rdd.foreachPartition((it: Iterator[(String, String, String, String, String, String)]) => {
        for (t6 <- it) {
          val jedis: Jedis = JedisUtil.getJedisConn;
          //统计ip出现次数
          if (!jedis.hexists(t6._1 + "-ips", t6._2))
            jedis.hset(t6._1 + "-ips", t6._2, 1L.toString)
          else {
            val num: Long = jedis.hget(t6._1 + "-ips", t6._2).toLong
            jedis.hset(t6._1 + "-ips", t6._2, (num + 1L).toString)
          }
          //统计搜索引擎次数
          if (!jedis.hexists(t6._1 + "-search", t6._3))
            jedis.hset(t6._1 + "-search", t6._3, 1L.toString)
          else {
            val num: Long = jedis.hget(t6._1 + "-search", t6._3).toLong
            jedis.hset(t6._1 + "-search", t6._3, (num + 1L).toString)
          }
          //统计某一位老师的所有课程被点击次数
          if (!jedis.hexists(t6._1 + "-teacher", t6._4))
            jedis.hset(t6._1 + "-teacher", t6._4, 1L.toString)
          else {
            val num: Long = jedis.hget(t6._1 + "-teacher", t6._4).toLong
            jedis.hset(t6._1 + "-teacher", t6._4, (num + 1L).toString)
          }
          //统计某一门课程被点击次数
          if (!jedis.hexists(t6._1 + "-courseNumb", t6._6))
            jedis.hset(t6._1 + "-courseNumb", t6._6, 1L.toString)
          else {
            val num: Long = jedis.hget(t6._1 + "-courseNumb", t6._6).toLong
            jedis.hset(t6._1 + "-courseNumb", t6._6, (num + 1L).toString)
          }
          //统计全国某一地区的访问总次数
          val ipLong: Long = IPUtil.ipToLong(t6._2)
          val address: String = IPUtil.searchIPRange(ipLong, broadcast.value)
          if (!jedis.hexists(t6._1 + "-address", address))
            jedis.hset(t6._1 + "-address", address, 1L.toString)
          else {
            val num: Long = jedis.hget(t6._1 + "-address", address).toLong
            jedis.hset(t6._1 + "-address", address, (num + 1L).toString)
          }
          jedis.close()
        }
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}

//ele(2).split('/')(4),ele(2).split('/')(5),ele(2).split('/')(6).split('.')(0)