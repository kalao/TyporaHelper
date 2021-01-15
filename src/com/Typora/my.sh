#!/bin/bash
echo "STARTING"
cd $1
echo "上传至github..."
$2sub.sh &
pid=$!
wait ${pid}
echo "上传结束..."
echo "FINISHED"