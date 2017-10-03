<?php

$data = scandir(__DIR__);
$data = array_filter($data, function($path) {
    return preg_match('|.json$|', $path);
});
$data = array_map(function($path) {
    return file_get_contents($path);
}, $data);
$data = array_filter($data, function($data) {
    return $data;
});

?><style>
ul { padding: 0; margin: 0; list-style: none; }
li { padding: 10px; background: #000; color: #aaa; margin: 10px; font-family: monospace; overflow-x: auto; }
.canvasContainer {
    background: transparent;
    padding: 0;
}
canvas {
    width: 250px;
    image-rendering: pixelated;
}
</style>
<ul id="pixels">
<?php foreach ($data as $item): ?>
  <li><?= preg_replace('|\s|', '', $item); ?></li>
<?php endforeach; ?>
</ul>

<script>
var list = document.getElementById('pixels');
list.addEventListener('click', function(event) {
    var target = event.target;
    if (target.nodeName !== 'LI') {
        return;
    }

    try {
       var data = JSON.parse(target.textContent);
   } catch (e) {
       console.error(e);
       return;
   }

   if (!data.pixels || !(data.pixels instanceof Array)) {
       console.error('Missing pixels array')
       return;
   }

   renderPixels(data.pixels, list, target);
});

function renderPixels(pixels, list, node) {
    var canvasContainer = list.querySelector('li[data-intent=canvas]');
    if (!canvasContainer) {
        canvasContainer = genCanvasContainer(28, 28);
    }

    list.insertBefore(canvasContainer, node.nextSibling);

    var canvas = canvasContainer.querySelector('canvas');
    var ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    var px = ctx.getImageData(0, 0, canvas.width, canvas.height);
    var max = Number.MAX_SAFE_INTEGER;
    var min = Number.MIN_SAFE_INTEGER;

    pixels.forEach(function(val) {
        if (val > min) {
            min = val;
        }

        if (val < max) {
            max = val;
        }
    });

    var tmp = max;
    max = min;
    min = tmp;

    pixels = pixels.map(function(val) {
        return 1 - (val - min) / (max - min);
    })

    for (var i = 0, _i = 0; i < pixels.length * 4; i += 4, _i += 1) {
        var val = pixels[_i];
        px.data[i] = px.data[i + 1] = px.data[i + 2] = 255 * val;
        px.data[i+3] = 255;
    }
    ctx.putImageData(px, 0, 0);
}

function genCanvasContainer(width, height) {
    var canvasContainer = document.createElement('li');
    canvasContainer.setAttribute('data-intent', 'canvas');
    canvasContainer.setAttribute('class', 'canvasContainer');

    var canvas = document.createElement('canvas');
    canvas.width = width;
    canvas.height = height;
    canvasContainer.appendChild(canvas);

    return canvasContainer;
}
</script>
