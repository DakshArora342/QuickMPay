



 function validateForm() {
            var aadharFrontPage = document.getElementById('aadharFrontPage').files.length;
            var aadharBackPage = document.getElementById('aadharBackPage').files.length;
            var panCard = document.getElementById('panCard').files.length;

            if (aadharFrontPage > 0 && aadharBackPage > 0 && panCard > 0) {
                document.getElementById('kycForm').submit();
            } else {
                alert('Please upload all images.');
            }
        }

      


document.getElementById('transfer-voucher').addEventListener('change', function() {
        var button = document.getElementById('paidButton');
        if (this.value) {
            button.removeAttribute('disabled');
            document.getElementById('paidMessage').innerText = '';
        } else {
            button.setAttribute('disabled', 'disabled');
            document.getElementById('paidMessage').innerText = 'Please upload a transfer voucher.';
        }
    });






function objectParshingAccountType(obj,rechargePoints){
    var data= JSON.parse(obj);
    
         var arrivalPoints = rechargePoints + (rechargePoints/100)*data.percentage ;
        var paymentAmount = rechargePoints / data.dollarRate;

        document.getElementById("arrival-points").value = arrivalPoints.toFixed(2);
        document.getElementById("payment-amount").value = paymentAmount.toFixed(2);

}




function update(accountType,rechargePoints){
    var xmphttp = new XMLHttpRequest();

    xmphttp.onreadystatechange = function () {
        if (xmphttp.readyState == 4 && xmphttp.status == 200) {
            objectParshingAccountType(xmphttp.responseText,rechargePoints);
        }
    }
    //xmphttp.setRequestHeader("Content-Type", "application/json");
xmphttp.open("GET", '/getRate?accountType=' + encodeURIComponent(accountType), true);
    xmphttp.send();
}





    function calculateAmount() {
		/*var percentage=10;
		var dollarRate=20;*/
        var rechargePoints = parseFloat(document.getElementById("recharge-points").value);
        var  accountType =document.getElementById("recharge-type").value;
       
        update(accountType,rechargePoints);
      
    }






const toggleSidebar = () => {
    if ($(".sidebar").is(":visible")) {
        //true
        //band Karna Hai
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%")
    } else {
        //false
        //show karna Hai
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%")

    }
}

const search = () => {
    console.log("Searching....")
    let query = $("#search-input").val();

    if(query==''){
        $(".search-result").hide();
    }
    else{
        // search
        console.log(query);

        // sending request to server
        let url = `http://localhost:8081/search/${query}`;

        fetch(url).then((response)=>{
            return response.json();
        }).then((data) =>{
            //data.....
            console.log(data)

            let text = `<div class="list-group">`;

            data.forEach(contact=>{
                text+=`<a href="/user/contact/${contact.cid}" class="list-group-item list-group-action">${contact.cname}</a>`
            });

            text+=`</div>`;

            $(".search-result").html(text);
            $(".search-result").show();
        })

    }
}