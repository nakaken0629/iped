$(function() {
  $("#faceForm input[type='button']").click(function() {
    var form = $("#faceForm").get(0);
    var formData = new FormData(form);

    $.ajax(form.action, {
      method: "POST",
      contentType: false,
      processData: false,
      data: formData,
      dataType: "text",
      success: function(faceKey) {
        $("#faceForm img").attr("src", "/backend/face/" + faceKey);
      },
    });

    return false;
  });
});
