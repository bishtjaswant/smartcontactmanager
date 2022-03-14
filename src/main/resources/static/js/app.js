 
//togles right sidebar

    function toggleRightBar( ) {
        
        if ($("#sidebar").is (":visible") ) {
            $("#sidebar").css("display", "none");
             
            $("#right-content").css("margin-left", "0");
        } else { 
            $("#sidebar").css("display", "block");
             
            $("#right-content").css("margin-left", "300px");
        }


    }



    /** searchQuery */

    const searchQuery = (   ) => {
        let keyword = $("#search-input").val();

        if (keyword=='') {
            $("#search-result").hide();
        } else {
            

            //ajjax call
          const  URL=`http://localhost:8080/smartcontactmanager/search/keyword`
            fetch(URL, {
                method: 'POST',
                body: keyword,
                   // credentials: 'include',
                    })
            .then(response => response.json())
            .then(data => { 
                console.log(data);
                let html= `  <ul class="list-group">`;
                   data.forEach(data => {
                       html+=`<a href="http://localhost:8080/smartcontactmanager/user/show-detail/${data.id}"> <li class="list-group-item text-capitalize">${data.name}</li> </a> `
                   });
                 
            html+= `</ul>`;
                $("#search-result").html(html);
                $("#search-result").show();
            })
            .catch(error => console.log(error));
        }
    }







