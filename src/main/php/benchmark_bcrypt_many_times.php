<?php

ini_set('memory_limit','32M');
$maxTimes = 50;

$cost = 10;
$times = 0;
$start = microtime(true);

for ($times = 0; $times < $maxTimes; $times++)
{
    password_hash("P@sw0rd!", PASSWORD_BCRYPT, ["cost" => $cost]);
}

$end = microtime(true);

echo "Time for $cost: " . ($end - $start)/$maxTimes, PHP_EOL;

