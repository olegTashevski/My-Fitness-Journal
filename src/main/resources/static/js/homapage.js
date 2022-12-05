
    var username= document.getElementById("username").innerHTML;
    console.log("hello lovly");
    var deletedImagesEdit=[];
    let i = 0;
    let lastPost = false;
    function getPostsForMainUser(){
      getPosts(i);
      setTimeout(appendDeleteButtonToPosts,300);     

    }

    function getPostsForFriends(){
      getPosts(i);
    }

    function getPostsForMainUserAfterScrolledBottom(){
      
  
        if(!lastPost)
      {
        timeoutKey = setTimeout(getPostsForMainUserAfterScrolledBottom,5000);
        if (window.innerHeight + window.pageYOffset >= document.body.offsetHeight) {
          i++;
          getPostsForMainUser();
        }
        }
        else{
          clearTimeout(timeoutKey)
        }
     
    
  }


  function getPostsForFriendsAfterScrolledBottom(){
   
    document.addEventListener('scroll', () => {
      if(!lastPost)
      {
        if (window.innerHeight + window.pageYOffset >= document.body.offsetHeight) {
          i++;
          setTimeout(getPostsForFriends,300);
        }
        }
    });
  
}

function getPosts(i){
  let elementPosts;
    try {
    elementPosts = document.getElementById("posts");
    
    } 
    catch (error) {
      return 0;
    }
  fetch("/getApi/getPosts?username="+username +"&pageIndex="+i+"&" + new Date().getTime(),{
    method:"GET",
    headers:{
      "Accept":"application/json"
    }
  }).then((response)=>{return response.json()})
  .then((posts)=>{
    
    for(const post of posts.content){
      let divPost = document.createElement("div");
      divPost.className="container";
      divPost.postId = post.id;
      let headPost = document.createElement("h3");
      let descriptionPost = document.createElement("p");
      headPost.innerHTML=post.head;
      descriptionPost.innerHTML=post.description;
      divPost.appendChild(headPost);
      divPost.appendChild(descriptionPost);
      appendPostImages(post.photos,divPost);
      elementPosts.appendChild(divPost);
    }
    lastPost=posts.last;
  })
  .catch(err=>alert(err));

}


function submitPostEdit(evt){
  let bodyI = new FormData();
  bodyI.append("postId",evt.currentTarget.postId);
  bodyI.append("newHead",document.getElementById("headEdit").value);
  bodyI.append("newDescription",document.getElementById("comment").value);
  let newImagesI = document.getElementById("imagesForPostEdit").files;
  if(newImagesI.length!=0){
    for(const image of newImagesI){}
    bodyI.append("newImages",newImagesI);
  }
 
  if(deletedImagesEdit.length!=0){
    bodyI.append("imagesIdsRemoved",deletedImagesEdit);
  }
  fetch("/postApi/editPost",{
    method:"POST",
    body:bodyI
  }).then(response=>response.text())
  .then(response=>console.log(response))
  .catch(err=>console.logg(err));
}




function appendPostImages(images,element){
  for(const image of images){
   let postImage =  document.createElement('img');
   postImage.style.width="250px"; 
   postImage.src="/getApi/getPostImage?photoId=" + image.id;
   element.appendChild(postImage);
  }
}

function deletePost(evt){
    fetch("/deleteApi/deletePost?id="+evt.currentTarget.postId,{
      method:"DELETE"
    }).then((response)=>response.text())
    .then((text)=>console.log(text))
    .catch((err)=>console.log(text));
    let post;
    while(post=evt.currentTarget.parentNode){
      if(post.className=="container"){
        post.remove();
        break;
      }
    }

   }

  function deleteImageFromEdit(evt){
    deletedImagesEdit.push(evt.currentTarget.imageId);
    let elementId = evt.currentTarget.divPhotoId;
    let element = document.getElementById(elementId);
    element.remove();
  }

   function editPost(evt){
    deletedImagesEdit=[];
    document.getElementById("modalId").innerHTML=' <div id="editPostDiv" class="container">'+
    '<div class="input-group mb-3">'+
  '<span class="input-group-text">Head</span>'+
'<input id="headEdit" type="text" class="form-control">'+
'</div>'+

      '<div class="mb-3 mt-3">'+
        '<label for="comment">Description:</label>'+
        '<textarea class="form-control" rows="5" id="comment" name="text"></textarea>'+
      '</div>'+
      '<p hidden id="postIdEdit"></p>'+
    '</div>';
    fetch("/getApi/getPost?id="+evt.currentTarget.postId,{
      method:"GET",
      headers:{
        "Accept":"application/json"
      }
    }).then(response=>response.json())
    .then(post=>{
      let submitButton = document.getElementById("SubmitEdit");
      submitButton.postId = post.id;
      submitButton.addEventListener("click",submitPostEdit);

      document.getElementById("headEdit").value= post.head;
      document.getElementById("comment").value= post.description;
      let contain = document.getElementById("editPostDiv");
      let before = document.getElementById("postIdEdit");
      let i = 1;
      for (const photoId of post.photosIds) {
        let divPhoto = document.createElement('div');
        divPhoto.id="divPhotosEdit"+i;
        let buttonDelete = document.createElement("button");
        buttonDelete.innerHTML="remove";
        buttonDelete.imageId = photoId;
        buttonDelete.divPhotoId = "divPhotosEdit"+i;
        buttonDelete.className="btn btn-secondary btn-sm btn btn-danger";
        buttonDelete.addEventListener("click",deleteImageFromEdit);
        let postImage =  document.createElement('img');
        postImage.style.width = "100px"; 
        postImage.src="/getApi/getPostImage?photoId=" + photoId;
        divPhoto.appendChild(buttonDelete);
        divPhoto.appendChild(postImage);
        contain.insertBefore(divPhoto,before);
        i++;
      }
    });




    

    
  }


 let counter=0;
function appendDeleteButtonToPosts(){
  counter*=5;
  let counterTemp = counter;
  let posts = document.getElementById("posts");
  for(let post of posts.childNodes){
    if(post.className==="container"){
    if(counterTemp){
      counterTemp--;
      continue;
    }
      let editButton = document.createElement("button");
      let buttonDelete = document.createElement("button");
      buttonDelete.innerHTML="delete";
      buttonDelete.className="btn btn-secondary btn-sm btn btn-danger";
      buttonDelete.postId=post.postId;
      editButton.innerHTML="edit";
      editButton.className = "btn btn-secondary btn-sm btn btn-warning"
      editButton.postId = post.postId;
      editButton.dataset.bsToggle="modal";
      editButton.dataset.bsTarget="#myModal";
      buttonDelete.addEventListener("click",deletePost);
      editButton.addEventListener("click",editPost);
      post.insertBefore(buttonDelete, post.firstChild);
       post.insertBefore(editButton, post.firstChild);
    }
  }
  counter++;
}




    (async function(){
      let ele = document.getElementById("friendRequest");
      let username = document.getElementById("username").innerHTML;
      try {
    let response = await fetch("/friendRequest/checkRequest?fromUser="+mainUser+"&toUser="+username,{
      method:"GET"
    });
    let piska = await response.text();
    if(piska=='true'){
      ele.innerHTML = "the Friend request has been sent";
    }
    else{
      ele.innerHTML = "SEND FRIEND REQUEST";
      document.getElementById("friendRequest").addEventListener("click",friendRequestTo);
    }
  } catch (error) {
    
  }
    })();

    let buttonForProfilePicEvent = ()=>{
      document.getElementById("customFile").hidden = false;
      document.getElementById("labelForProfilePicInput").hidden = false;
      let element = document.getElementById("buttonForProfilePic");
      element.replaceWith(element.cloneNode(true));
      element = document.getElementById("buttonForProfilePic");
      element.innerHTML='Submit profile pic';
      element.addEventListener("click", async ()=>{
        let image = document.getElementById("customFile").files[0];
        let formImage = new FormData();
        formImage.append("image",image);
        fetch('/postApi/addProfilePic?username='+username,{
          method:"POST",
          body:formImage
        }).then((response) =>{
          return response.text();})
          .then((response)=>{
            document.getElementById("profileImage").src='/getApi/ProfileImage?weightlifterId='+username +"&" + new Date().getTime();
            console.log(response);
            document.getElementById("customFile").hidden = true;
            document.getElementById("labelForProfilePicInput").hidden = true;
            let element = document.getElementById("buttonForProfilePic");
            element.replaceWith(element.cloneNode(true));
            element = document.getElementById("buttonForProfilePic");
            element.innerHTML='Update profile pic';
            element.addEventListener("click",buttonForProfilePicEvent);

            
          })
          .catch((err)=>console.log(err));
      })
    };
    document.getElementById("buttonForProfilePic").addEventListener("click", buttonForProfilePicEvent);
    
    function searchBarClick(evt){
      let otherUser =  evt.currentTarget.innerHTML;
      window.location.replace("/getHomapageForSearchBar?myUsername="+username+"&otherUsername="+otherUser);

    }

    function showResult(val) {
      let username = document.getElementById("username").innerHTML;
      res = document.getElementById("SuggestionRezult");
      res.innerHTML = '';
      if (val == '') {
        return;
      }
      let list = document.createElement('ul');
      fetch('/getApi/getWeighliftersUsernamesForPattern?pattern=' + val).then(
       function (response) {
         return response.json();
       }).then(function (data) {
         for (i=0; i<data.content.length; i++) {
          let li = document.createElement("li");
         
          let buttonI = document.createElement("a");
          buttonI.innerHTML = data.content[i];
          buttonI.addEventListener("click",searchBarClick);
          li.appendChild(buttonI);
          list.appendChild(li);
         }
         res.appendChild(list);
         return true;
       }).catch(function (err) {
         console.warn('Something went wrong.', err);
         return false;
       });
    }

      async function post(){

//      let header = document.getElementById("Header").value;
//      let descriptionInput = document.getElementById("descriptionForPost").value;
        let imagesInput = document.getElementById("imagesForPost").files;
        let hasImages = document.getElementById("hasImages");
//      let data = new FormData();
//      data.append("weightlifterUsername",username);
//      if(header!=null ||header!=''){
//      data.append("head",header);
//      }
//
//      data.append("description",descriptionInput);
          if(imagesInput.length == 0){
            hasImages.value='false';
          }
//        for (const file of imagesInput) {
//          data.append("images",file);
//        }
//      }
//        try{
//        let response = await fetch("/postApi/post",{
//          method:"POST",
//          body:data
//        });
//      }
//      catch(err){
//        console.log(err);
//      }
//
//        response = await response.text();
//        console.log(response);

    }

     async function createMouthlyShedule(){
      let username= document.getElementById("username").innerHTML;
      try {
      let response = await fetch("/postApi/addMouthlySchedule?us="+username,{
        method:"POST"
      });
        var json = await response.json(); 
      } catch (error) {
        console.log(error);
      }
		console.log(json);
      window.location.replace("/homepage?username="+username);
 

    }

    async function friendRequestTo(){
      let username = document.getElementById("username").innerHTML;
      let json;
      try {
        let response = await fetch("/friendRequest/sendRequest?fromUser="+mainUser+"&toUser="+username,{
          method:"POST"
        });
         json = await response.json();
      } catch (error) {
        console.log(error);
      }
      console.log(json);
      this.innerHTML="Friend request send";
      this.parentNode.replaceChild(this.cloneNode(true),this);
    }

    async function getFriendRequests(){
      
      try {
        let response = await fetch("/friendRequest/getUserRequests?username="+username,{
          method:"GET"
        }); 

        response = await response.json();
        list = document.getElementById("GetfriendRequests");
        while (list.firstChild) {
        list.removeChild(list.firstChild);
        }
        for (const request of response) {
          let li = document.createElement("li");
          let imageI = document.createElement("img");
          imageI.src="/getApi/ProfileImage?weightlifterId="+request.fromUsername;
          imageI.style="width: 100px;"
          let he6 = document.createElement("h6");
          he6.innerHTML = request.fromUsername;
          let buttonAccept = document.createElement('button');
          let buttonDecline = document.createElement('button');
          buttonAccept.addEventListener("click", async ()=>{
            const answer = {from:request.fromUsername,to:request.toUsername,accepted:true};
            let response = await fetch("/friendRequest/resolveRequest",{
              method:"POST",
              body:JSON.stringify(answer),
              headers: {
              'Content-Type': 'application/json;charset=utf-8'
              }

            });
            console.log(await response.text());
            getFriendRequests();
            addfriend(request.fromUsername);
          });
          buttonDecline.addEventListener("click", async ()=>{
             const answer = {from:request.fromUsername,to:request.toUsername,accepted:false};
            let response = await fetch("/friendRequest/resolveRequest",{
              method:"POST",
              body:JSON.stringify(answer),
              headers: {
              'Content-Type': 'application/json;charset=utf-8'
              }

            });
            console.log(await response.text());
            getFriendRequests();
          });
          buttonAccept.className="btn btn-success";
          buttonDecline.className="btn btn-danger";
          buttonAccept.innerHTML="Accept";
          buttonDecline.innerHTML="Dicline";
          li.appendChild(imageI);
          li.appendChild(he6);
          li.appendChild(buttonAccept);
          li.appendChild(buttonDecline);
          list.appendChild(li);
        }
    }
    catch(error){
      console.log(error);
    }
  }

  function addfriend(friendUsername){
    
    let list = document.getElementById("myFriends");
    let li = document.createElement("li");
    let ul = document.createElement("ul");
    ul.style="padding-left: 0px; text-align: left;";
    let liForUl = document.createElement("li");
    liForUl.style="display:inline-block;padding: 10px 20px;";
    let img = document.createElement("img");
    img.style="width: 100px;";
    img.src='/getApi/ProfileImage?weightlifterId='+friendUsername;
    img.alt="no profile pic";
    let h4I = document.createElement("h4");
    let aForH4 = document.createElement("a");
    aForH4.href='/getHomapageForFriend?myUsername='+username+'&friendUsername='+friendUsername;
    aForH4.innerHTML=friendUsername;
    h4I.appendChild(aForH4);
    liForUl.appendChild(img);
    liForUl.appendChild(h4I);
    ul.appendChild(liForUl);
    li.appendChild(ul);
    list.appendChild(li);

  }


  function deleteFriend(element){
    let usernameFriend = element;
    while(usernameFriend = usernameFriend.nextSibling){
      if(usernameFriend.className=="friendUsername"){
        usernameFriend = usernameFriend.innerHTML;
        break;
      }
    }
    fetch("/deleteApi/deleteFriend?username1="+username+"&username2="+usernameFriend,{
      method:"DELETE"
    }).then( response => response.text())
    .then(response => console.log(response))
    .catch(err=>console.log(err));
    while(element=element.parentNode){
      if(element.className=="friendInfo"){
        element.remove();
      }
    }

  }


 function changeBio(){
  let bio = document.getElementById('bio');
  let div = document.createElement('div');
  div.className='form-outline';
  div.id='divBioTextarea';
  let txtarea = document.createElement('textarea');
  txtarea.value = bio.innerHTML;
  txtarea.className='form-control';
  txtarea.id='txtareaBio';
  txtarea.rows=4;
  let label = document.createElement('label');
  label.className='form-label';
  label.setAttribute('for','txtareaBio');
  label.innerHTML = '';
  let button = document.createElement('button');
  button.innerHTML='submit change';
  button.className="btn btn-secondary btn-sm";
  button.addEventListener('click',evt=>{
    let newBio = document.getElementById('txtareaBio').value;
    fetch('/putApi/updateBio?username='+username+'&newBio='+newBio,{
      method:"PUT"
    }).then(response=>response.text())
    .then(text=>{console.log(text);
      let bioDiv = document.createElement('div');
      bioDiv.id='bioDiv';
      let p = document.createElement('p');
      p.innerHTML=newBio;
      p.id='bio';
      let button = document.createElement('button');
      button.innerHTML='change bio';
      button.className='btn btn-secondary btn-sm btn btn-warning';
      button.addEventListener('click',changeBio);
      bioDiv.appendChild(p);
      bioDiv.appendChild(button);
      let replaceTextarea = document.getElementById('divBioTextarea');
      replaceTextarea.parentNode.replaceChild(bioDiv,replaceTextarea);
    })
    .catch(err=>{alert("your bio length cannot be longer than 80 characters ");console.log(err);
    let bioDiv = document.createElement('div');
      bioDiv.id='bioDiv';
      let p = document.createElement('p');
      p.innerHTML=bio.innerHTML;
      p.id='bio';
      let button = document.createElement('button');
      button.innerHTML='change bio';
      button.className='btn btn-secondary btn-sm btn btn-warning';
      button.addEventListener('click',changeBio);
      bioDiv.appendChild(p);
      bioDiv.appendChild(button);
      let replaceTextarea = document.getElementById('divBioTextarea');
      replaceTextarea.parentNode.replaceChild(bioDiv,replaceTextarea);
  });
    
  });
  div.appendChild(txtarea);
  div.appendChild(label);
  div.appendChild(button);
  let replaceBioDiv = document.getElementById('bioDiv');
  replaceBioDiv.parentNode.replaceChild(div,replaceBioDiv);


 }

 function showErrors(errors){
  alert(errors);
 }

 function imageNotException(){
  alert("The following file  was not a image,please enter only images in your post");
 }

  
