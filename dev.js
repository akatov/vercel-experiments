const express = require('express');
const api = require('./api');

var port = 3000;
var portIsNext = false;

process.argv.forEach(function (val, index, array) {
  if (portIsNext) {
    port = val;
  }
  if (val === "--port") {
    portIsNext = true;
  }
});

const app = express();

app.use('/api', api);
app.use(express.static('public'));

app.listen(port, () => {
  console.log(`Development app is ready on port ${port}`);
});
