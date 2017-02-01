<?php

ini_set('memory_limit','32M');
$allowedTime =0.09; // 100 milliseconds

$cost = 10000;
for ($cost = 10000, $start = 0, $end = 0; ($end - $start) < $allowedTime; $cost+=2000)
{
    $salt = "$6\$rounds=$cost\$"."fOIreten3OGkiKTJ\$";

    $start = microtime(true);
    crypt("P@sw0rd!", $salt);
    $end = microtime(true);
    echo "cost $cost, ".($end - $start), PHP_EOL;
}
