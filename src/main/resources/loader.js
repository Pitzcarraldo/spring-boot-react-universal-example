module.exports = function load(path, model, callback) {
  var render = require(path);
  model = JSON.parse(model);
  if (render.length === 2) {
    render(model, function (response) {
      callback && callback(toString(response))
    });
    return '';
  }
  return toString(render(model));
};

function toString(response) {
  if (typeof response !== 'object') {
    response = { headers: {}, body: response };
  }
  return JSON.stringify(response)
}