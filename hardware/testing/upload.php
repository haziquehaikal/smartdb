<?php

// require ("config.php");

//connect to MySQL

// $conn = new mysqli("localhost", "root", "", "testpiqs");
// // Check connection
// if ($conn->connect_error) {
//     die("Connection failed: " . $conn->connect_error);
// }

$data = json_decode(file_get_contents('php://input'), true);
// $sql = "INSERT INTO inbox (deviceID, img)
// VALUES ('".$data["deviceID"]."','". $data['image_data'] . "')";

// echo '<img src="'. $data['image_data'] . '" />';

// if ($conn->query($sql) === TRUE) {
//     echo "Image Data successfully insert";
//     echo $sql;

// } else {
//     echo "Error: " . $sql . "<br>" . $conn->error;
// }

// $conn->close();

if ($data["image_data"]) {
    $res = array(
        'image' => true,
    );

    echo json_encode($res);
} else {
    $res = array(
        'image' => false,
    );

    echo json_encode($res);

}
