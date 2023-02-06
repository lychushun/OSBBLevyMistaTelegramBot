$(document).ready(function () {

    const urlParams = new URLSearchParams(window.location.search);
    const chatId = urlParams.get('chatId')
    console.log("chatId: " + chatId);

    $(".btn-success").click(function () {

        const email = $("#email").val()
        const password = $("#password").val()

        $.ajax({
            type: "POST",
            url: "https://192.168.88.19:8080/api/auth",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify({
                pass:password,
                login:email,
                chatId:chatId
            }),
            success: function() { alert("Success"); },
            error: function() { alert('Failed!'); },
        });
    })

});
