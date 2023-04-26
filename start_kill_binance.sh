#!/bin/bash
echo "************ kill java , Begin... **************"
str=`ps -e | grep -w java | awk '{print $1}'`
if [ -z "$str" ]; then
   echo "var is empty"
   exit 0
else
   echo $str muchjoydao
kill -9 $str
if [ "$?" -eq 0 ]; then
    echo "kill success"
else
    echo "kill failed"
fi
echo "************ kill Success and start... *********"

nohup java -jar muchjoydao.jar > nohup.out 2>&1 &
tail -f nohup.out

fi
