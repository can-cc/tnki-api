var child_process = require('child_process');
var colors = require('colors');

var result = child_process.exec('node test-out/test');

console.log('» » » Testing:'.bold.yellow);

result.stdout.on('data', function(data) {
  console.log(data.replace('@Success@', '✔ Succcess'.bold.green));
});

result.stderr.on('data', function(data) {
  console.error(data.bold.red);
});

result.on('close', code => {
  process.exit(code);
});
