cd $1
$2localsub.sh &
pid=$!
wait ${pid}