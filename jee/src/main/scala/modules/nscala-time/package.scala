package modules

// API Doc: http://bit.ly/nscala-time
package object `nscala-time` {
  import com.github.nscala_time.time.Imports._

  def nscalaTimeExample:Unit = {
    DateTime.now + 2.months // returns org.joda.time.DateTime = 2009-06-27T13:25:59.195-07:00

    DateTime.nextMonth < DateTime.now + 2.months // returns Boolean = true

    DateTime.now to DateTime.tomorrow  // return org.joda.time.Interval = > 2009-04-27T13:47:14.840/2009-04-28T13:47:14.840

    (DateTime.now to DateTime.nextSecond).millis // returns Long = 1000

    2.hours + 45.minutes + 10.seconds
    // returns com.github.nscala_time.time.DurationBuilder
    // (can be used as a Duration or as a Period)

    (2.hours + 45.minutes + 10.seconds).millis
    // returns Long = 9910000

    2.months + 3.days
    // returns Period
  }
}
