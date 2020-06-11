<?php

header("Content-Type: application/json; charset=UTF-8");

require_once 'access/config.php';

$sql = "SELECT * from tabel_pegawai";
$result = mysqli_query($con, $sql);
$response = array();

$server_name = $_SERVER['SERVER_ADDR'];

while ($row = mysqli_fetch_assoc($result)) {

    array_push(
        $response,
        array(
            'id' => $row['id'],
            'nama' => $row['nama'],
            'posisi' => $row['posisi'],
            'gaji' => $row['gaji'],
            'email' => $row['email'],
            'nphone' => $row['nphone'],
            'alamat' => $row['alamat'],
            'gender' => $row['gender'],
            'gambar' => "http://$server_name".$row['gambar'],
        )
    );
}

echo json_encode($response);
mysqli_close($con);
