package edu.xust.bigdata.utils

object IPUtil {
  def ipToLong(ip: String): Long = {
    val ipSplit: Array[String] = ip.split('.')
    var ipLong = 0L
    for (ele <- ipSplit) {
      ipLong = ele.toLong | ipLong << 8L
    }
    ipLong
  }
  //Array ((0):ipLong_Min  (1):ipLong_Max  (2):address)
  def searchIPRange(ipLong: Long, bsArray: Array[(String, String, String)]): String = {
    var hight = bsArray.length - 1
    var low = 0
    while (low <= hight) {
      var mid = (low + hight) / 2
      if (ipLong >= bsArray(mid)._1.toLong && ipLong <= bsArray(mid)._2.toLong) {
        return bsArray(mid)._3
      } else if (ipLong < bsArray(mid)._1.toLong) {
        hight = mid - 1
      } else if (ipLong > bsArray(mid)._2.toLong) {
        low = mid + 1
      }
    }
    null
  }

}
