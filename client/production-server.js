const express = require('express');
const proxy = require('http-proxy-middleware');
const port = 8080;

const app = express();
const path = require('path');
const buildPath = path.join(__dirname, 'dist');

const proxyOptions = require('./config/proxy-table');

app.use(express.static(buildPath));

Object.keys(proxyOptions).forEach(path => {
  app.use(path, proxy(proxyOptions[path]));
});

app.use((req, res) => res.sendFile(path.join(buildPath, 'index.html')));
app.listen(port);
console.log(`Built app serve at http://127.0.0.1:${port}`);
