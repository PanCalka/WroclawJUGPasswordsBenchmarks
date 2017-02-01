<?php
ini_set('memory_limit','32M');
$allowedTime = 0.09; // 100 milliseconds

for ($cost = 4, $start = 0, $end = 0; ($end - $start) < $allowedTime; $cost++)
{
    $start = microtime(true);
    password_hash("P@sw0rd!", PASSWORD_BCRYPT, ["cost" => $cost]);
    $end = microtime(true);
    echo "cost $cost, ".($end - $start), PHP_EOL;
}

