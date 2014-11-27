$(function() {
  $("#remarkButton").click(function() {
    var remark = $("#remark").val().trim();
    if (remark == "") {
      return false;
    }
    var tokenId = $("#tokenId").val();
    var patientId = $("#patientId").val();

    var request = {
        text: remark
    };

    $.ajax("/api/secure/remarks/new", {
      method: "POST",
      headers: {
        'X-IPED-TOKEN-ID': tokenId,
        'X-IPED-PATIENT-ID': patientId,
      },
      data: {parameter: JSON.stringify(request)},
      dataType: "json",
      success: function(response) {
        location.reload();
      },
      error: function(XMLHttpRequest, textStatus, errorThrown) {
        alert('送信に失敗しました');
      },
    });

    return false;
  });
});
