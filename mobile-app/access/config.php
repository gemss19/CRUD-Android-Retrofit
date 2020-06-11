<?php
define('Host', 'localhost');
define('user', 'root');
define('pass', '');
define('db', 'db_android');

$con = mysqli_connect(Host, user, pass, db) or die('Unable To Connect!');

// fungsi random string pada gambar untuk menghindari nama file yang sama
function random_word($x = 20)
{
    $pool = '1234567890abcdefghijkmnpqrstuvwxyz';

    $word = '';
    for ($i = 0; $i < $x; $i++) {
        $word .= substr($pool, mt_rand(0, strlen($pool) - 1), 1);
    }
    return $word;
}
