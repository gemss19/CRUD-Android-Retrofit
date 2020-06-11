<?php

require_once 'access/config.php';

$key = $_POST['key'];

$name = $_POST['nama'];
$desg = $_POST['posisi'];
$sal = $_POST['gaji'];
$eml = $_POST['email'];
$phn = $_POST['nphone'];
$almt = $_POST['alamat'];
$gen = $_POST['gender'];
$img = $_POST['gambar'];

if ($key == "insert") {
    $sql = "INSERT into tabel_pegawai (nama,posisi,gaji,email,nphone,alamat,gender) values ('$name','$desg','$sal','$eml','$phn','$almt','$gen')";

    if (mysqli_query($con, $sql)) {
        if ($img == null) {
            $finalPath = "/mobile-app/user_logo.png";
            $result["value"] = "1";
            $result["message"] = "error!";

            echo json_encode($result);
            mysqli_close($con);
        } else {
            $id = mysqli_insert_id($con);

            $path = "img/$id.png";
            $finalPath = "/mobile-app/" . $path;

            $insert_img = "UPDATE tabel_pegawai set gambar='$finalPath' where id='$id'";

            if (mysqli_query($con, $insert_img)) {

                if (file_put_contents($path, base64_decode($img))) {
                    $result["value"] = "1";
                    $result["message"] = "Success!";

                    echo json_encode($result);
                    mysqli_close($con);
                } else {
                    $response["value"] = "0";
                    $response["message"] = "Error! " . mysqli_error($con);

                    echo json_encode($response);
                    mysqli_close($con);
                }
            }
        }
    } else {
        $response["value"] = "0";
        $response["message"] = "Error! " . mysqli_error($con);

        echo json_encode($response);
        mysqli_close($con);
    }
}
