<?php
$externalHash = '$6$$Y8Et6wWDdXO2tJZRabvSfQvG2Lc8bAS6D9COIsMXEJ2KjA27wqDuAyd/CdazBQc3H3xQX.JXMKxJeRz2OqTkl.';
$localHash = crypt("opnsense", $externalHash);
echo $externalHash, PHP_EOL;
echo $localHash, PHP_EOL;
echo $externalHash === $localHash , PHP_EOL;
