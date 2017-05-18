(function () {
    var $date = document.getElementById('js-date');
    $date.innerHTML = String(new Date().toISOString().slice(0,10).replace(/-/g,"."));
}());
