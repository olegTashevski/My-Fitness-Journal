async function signUp(){
  alert("Helo");
  let genderInputs = document.getElementsByName("gender");
  for (const inp of genderInputs) {
    if(inp.checked){
      document.getElementById("genderInput")=inp.value;
    }
  }

//    console.log("hello");
//    const body1 = {username:"",password:"",firstname:"",lastName:"",gender:"",dateOfBirth:null};
//    let map = new Map();
//      let  serverResponse;
//
//     var all = document.querySelectorAll("#form_sign_up input");
//     for (let field of all) {
//        
//        if (field.type != "submit") { 
//          
//          if (field.type=="radio") {
//            if (field.checked) {map.set(field.name,field.value) }
//          }
//          else {map.set(field.name,field.value)}
//        }
//      }
//      body1["username"] = map.get("usernameUp");
//      body1["password"] = map.get("passwordUp");
//      body1["firstname"] = map.get("firstName");
//      body1["lastName"] = map.get("lastName");
//      body1["gender"] = map.get("gender");
//      body1["dateOfBirth"] = map.get("dateOfBirth");
//      let bodyJSON = JSON.stringify(body1);
//      var sendRequset = new XMLHttpRequest();
//      sendRequset.open("POST","/postApi/addUser");
//      sendRequset.setRequestHeader('Accept','application/json');
//      sendRequset.setRequestHeader('Content-Type','application/json');
//      sendRequset.onload = function(){
//        allert("Yeeee");
//      };
//      sendRequset.send(bodyJSON);
//


}

 function showSignpErrors(message){
  alert(message);
}

