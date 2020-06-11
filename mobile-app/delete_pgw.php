<?php
header("Content-Type: application/json; charset=UTF-8");

require_once 'access/config.php';

$key = $_POST['key'];
$id = $_POST['id'];
$img = $_POST['gambar'];

if ($key == "delete") {
    $sql = "DELETE from tabel_pegawai where id='$id'";

    if (mysqli_query($con, $sql)) {
        $iparr = explode("/", $img);
        $img_split = $iparr[5];

        if (unlink("img/" . $img_split)) {
            $result["value"] = "1";
            $result["message"] = "Success!";

            echo json_encode($result);
            mysqli_close($con);
        } else {
            $response["value"] = "0";
            $response["message"] = "Error to delete image!" . mysqli_error($con);

            echo json_encode($response);
            mysqli_close($con);
        }
    } else {
        $response["value"] = "0";
        $response["message"] = "Error!" . mysqli_error($con);

        echo json_encode($response);
        mysqli_close($con);
    }
} else {
    $response["value"] = "0";
    $response["message"] = "Error to delete image!" . mysqli_error($con);

    echo json_encode($response);
    mysqli_close($con);
}
