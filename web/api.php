<?php

function post($url, $body) {
    $ch = curl_init($url);

    curl_setopt_array($ch, [
        CURLOPT_POST => TRUE,
        CURLOPT_RETURNTRANSFER => TRUE,
        CURLOPT_HTTPHEADER => [
            'Content-Type: application/json'
        ],
        CURLOPT_POSTFIELDS => json_encode($body),
    ]);

    $response = curl_exec($ch);
    $error = '';

    if($response === FALSE){
        $error = curl_error($ch);
    }

    curl_close($ch);
    $json = json_decode($response, true);

    if ($error) {
        return ['error' => $error];
    }

    if (empty($json)) {
        return ['error' => 'Could not decode response.', 'response' => $response];
    }

    return $json;
}


$data = file_get_contents('php://input');

if ($data) {
    file_put_contents(__DIR__.'/req-' . time(). '.json', $data);
}

$respCode = 200;
$data = json_decode($data, true);
$resp = [
   'time' => time(),
   'prettyTime' => date('Y-m-d H:i:s', time()),
];

if ($data) {
    $post = post('http://localhost:6006', $data);
    if (array_key_exists('error', $post)) {
        $respCode = 500;
        $resp = array_merge($resp, $post);
    } else {
        $resp['response'] = $post;
    }
} else {
    $respCode = 500;
    $resp['error'] = 'Could not decode input.';
}

header('content-type: application/json');
http_response_code($respCode);
echo json_encode($resp);
