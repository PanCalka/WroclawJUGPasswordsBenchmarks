<?php

ini_set('memory_limit','32M');
$allowedTime =0.09; // 100 milliseconds

for ($cost = 4, $start = 0, $end = 0; ($end - $start) < $allowedTime; $cost++)
{
    $paddedCost = sprintf('%02d', $cost);
    $salt = "$2y\$$paddedCost\$"."1NSywRX1tOUMpxjjQzHRN\$";

    $start = microtime(true);
    crypt("P@sw0rd!", $salt);
    $end = microtime(true);
    echo "cost $cost, ".($end - $start), PHP_EOL;
}
