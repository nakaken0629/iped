$(function() {
  $("#patientId").change(function() {
    var tokenId = $("#tokenId").val();
    var patientId = $("#patientId").val();

    $.ajax("/web/secure/patient", {
      method: "POST",
      headers: {
        'X-IPED-TOKEN-ID': tokenId,
        'X-IPED-PATIENT-ID': patientId,
      },
      dataType: "text",
      success: function(response) {
        location.reload();
      },
      error: function(XMLHttpRequest, textStatus, errorThrown) {
        alert('送信に失敗しました' + '(' + textStatus + ')');
      },
    });

    return false;
  });
});
