ecse211 - design principle methods
=======

###lab 2 

light sensor correction algo:

```java
 move forward ;
boolean firstLineDetection = false ;

while (true){
      if (Odometer.getPositive() == true){
      	 firstLineDetection = true ;