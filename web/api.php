<?php

$data = file_get_contents('php://input');

if ($data) {
    file_put_contents(__DIR__.'/req-' . time(). '.json', $data);
}

$resp = [
   'response' => json_decode($data, true),
   'time' => time(),
   'prettyTime' => date('Y-m-d H:i:s', time()),
];

$respCode = 200;
if ($resp['response'] === null) {
    $respCode = 500;
}

header('content-type: application/json');
http_response_code($respCode);
echo json_encode($resp);
