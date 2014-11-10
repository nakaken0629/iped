$(function() {
  $("#talkButton").click(function() {
    var talk = $("#talk").val().trim();
    if (talk == "") {
      return false;
    }
    var tokenId = $("#tokenId").val();

    var request = {
        text: talk
    };

    $.ajax("/api/secure/talks/new", {
      method: "POST",
      headers: {
        'X-IPED-TOKEN-ID': tokenId,
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
