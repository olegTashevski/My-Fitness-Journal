var ingridients = [];
var countOfIngridients = 2;
var currentDay;
var exercisesW = [];
var numberOfExercises = 1;
function diffrentDay(element){
    let dayId;
    while(true){
        element = element.parentNode;
        if(element.className=="dayList"){
            break;
        }
    }
    for (const el of element.childNodes ) {
        if(el.className == "dayId"){
           dayId = el.innerHTML;
           break;
        }
    }
    if(currentDay!=dayId){
        countOfIngridients=2;
        ingridients=[];
        exercisesW=[];
        numberOfExercises = 1;
    }
}
function insertIng(event,elementButton){
    event.preventDefault();
    let elementInput;
    let nodes = elementButton.parentNode.childNodes;
    for (const it of nodes) {
        if(it.tagName=="INPUT"){
            ingridients.push(it.value);
            it.placeholder="Ingridient " + countOfIngridients;
            countOfIngridients+=1; 
            elementInput =it;
            break;
        }
    }
    for (const el of elementButton.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.childNodes) {
        if(el.className == "dayId"){
            currentDay = el.innerHTML;
            break;
        }
    }
    elementInput.value="";

}
async function addMeal(element,event){
    event.preventDefault();
    let name;
    let typeInput;
    let caloriesI;
    let proteinI;
    let carbsI;
    let fatsI;
    let nodes = element.childNodes;
    for (const node of nodes) {
        if(node.tagName=="SELECT"){
            let option = node.options[node.selectedIndex];
            typeInput = option.text;
        }
        if(node.tagName=="DIV"){
            if(node.class="input-group mb-3 input-group-sm"){
               for (const child of node.childNodes) {
                    if(child.tagName=="SPAN" && child.innerHTML=="Name"){
                        name = child.nextSibling.nextSibling.value;
                        break;
                    } 
               }
            }
            if(node.class="form-outline"){
                for (const child of node.childNodes) {
                    if(child.tagName=="LABEL" && child.innerHTML=="Calories"){
                        caloriesI = child.nextSibling.nextSibling.value;
                        
                    }
                    if(child.tagName=="LABEL" && child.innerHTML=="Protein in grams"){
                        proteinI = child.nextSibling.nextSibling.value;
                        
                    }
                    if(child.tagName=="LABEL" && child.innerHTML=="Carbs in grams"){
                        carbsI = child.nextSibling.nextSibling.value;
                        
                    }
                    if(child.tagName=="LABEL" && child.innerHTML=="Fats in grams"){
                        fatsI = child.nextSibling.nextSibling.value;
                        
                    }

               }
            }

        }
    }
    let username;
    let dayId;
    for (const el of element.parentNode.parentNode.parentNode.parentNode.parentNode.childNodes ) {
        if(el.className == "dayId"|| el.className == "username"){
            if(el.className=="dayId"){
                dayId=el.innerHTML;
            }
            else{
                username=el.innerHTML;
            }
        }
    }

    try {
        let response = await fetch("/postApi/addMeal",{
          method:"POST",
          body:JSON.stringify({usernameWeightlifter:username,idofDay:dayId,type:typeInput,nameOfMeal:name,ingredients:ingridients,calories:caloriesI,protein:proteinI,carbs:carbsI,fats:fatsI}),
          headers: {
            'Content-Type': 'application/json;charset=utf-8'
          }
            
        });
        console.log(response.text());
          window.location.replace("/getMonthlySheduleU?username="+username); 
        } catch (error) {
          console.log(error);
          alert("Invalid information about the  Meal submited ");
        }
        

          

        


}

function inputForReps(element){
    let input = element.value;
    let divElement;
    let tempElement;
    while(true) {
        element=element.parentNode;
        if(element.tagName=='DIV'){
            divElement = element;
            while(true){
                element = element.nextSibling;
                if(element==null || element.className=='RepsList'){
                    if(element==null){
                        break;
                    }
                    element.remove();
                    break;
                }
                
            }
            break;
        }
    }
    let list = document.createElement("UL");
    list.className='RepsList';
    for(let i = 0;i<input;i++){
        let li = document.createElement("LI");
        let input = document.createElement("INPUT");
        input.placeholder='Reps for Set'+(i+1);
        input.className='Reps';
        input.type="number";
        li.appendChild(input);
        list.appendChild(li);
    }
    divElement.parentNode.insertBefore(list, divElement.nextSibling);

}

function insertExercise(element){
    let elementCheck = element;
    let formE;
    let nameE;
    let setsE;
    let repsE = [];
    while(true){
        formE=element.parentNode;
        if(formE.tagName=='FORM'){
            break;
        }
    }
    for (const node of formE.childNodes) {
        if(node.className=='input-group mb-3 input-group-sm'){
            for(const input of node.childNodes){
                if(input.tagName=='INPUT'){
                    nameE= input.value;
                    input.value = '';
                }
            }
        }
        if(node.className=='form-outline'){
            for(const input of node.childNodes){
                if(input.tagName=='INPUT'){
                    setsE = input.value;
                    input.value = '';
                    break;
                }
            }
        }
        if(node.className=='exercise'){
            numberOfExercises+=1;
            node.innerHTML = "Exercise"+numberOfExercises;
        }
        
    }
    for (const re of document.getElementsByClassName("Reps")) {
        repsE.push(re.value);
        re.value = '';
    }
    const exercise = {name:nameE,sets:setsE,reps:repsE};
    exercisesW.push(exercise);

    while(true){
        elementCheck = elementCheck.parentNode;
        if(elementCheck.className=="dayList"){
            break;
        }
    }
    for (const el of elementCheck.childNodes ) {
        if(el.className == "dayId"){
            currentDay = el.innerHTML;
           break;
        }
    }

}

 async function sumbitWokrout(element,event){
    event.preventDefault();
    let username;
    let dayId;
    while(true){
        element = element.parentNode;
        if(element.className=="dayList"){
            break;
        }
    }
    for (const el of element.childNodes ) {
        if(el.className == "dayId"|| el.className == "username"){
            if(el.className=="dayId"){
                dayId=el.innerHTML;
            }
            else{
                username=el.innerHTML;
            }
        }
    }

    try {
        let response = await fetch("/postApi/addWorkout",{
          method:"POST",
          body:JSON.stringify({usernameWeightlifter:username,idofDay:dayId,exercises:exercisesW}),
          headers: {
            'Content-Type': 'application/json;charset=utf-8'
          }
            
        });
        console.log(response.text()); 
          window.location.replace("/getMonthlySheduleU?username="+username);
        } catch (error) {
          console.log(error);
          alert("Invalid information about the workout submited");
        }
          
          
 }

 async function sumbitSupplement(element,event){
    event.preventDefault();
    let formE = element;
    let username;
    let dayId;
    let supplementI;
    while(true){
        element = element.parentNode;
        if(element.className=="dayList"){
            break;
        }
    }
    for (const el of element.childNodes ) {
        if(el.className == "dayId"|| el.className == "username"){
            if(el.className=="dayId"){
                dayId=el.innerHTML;
            }
            else{
                username=el.innerHTML;
            }
        }
    }
    for (const node of formE.childNodes) {
        if(node.className=='input-group mb-3 input-group-sm'){
            for(const input of node.childNodes){
                if(input.tagName=='INPUT'){
                    supplementI= input.value;
                    input.value = '';
                    break;
                }
            }
            break;
        }
    }
    try {
        let response = await fetch("/postApi/addSupplement",{
          method:"POST",
          body:JSON.stringify({usernameWeightlifter:username,idofDay:dayId,supplement:supplementI}),
          headers: {
            'Content-Type': 'application/json;charset=utf-8'
          }
            
        });


          console.log(await response.text());
          window.location.replace("/getMonthlySheduleU?username="+username); 
        } catch (error) {
          console.log(error);
          alert("invalid supplement submited");
        }
          

          window.location.replace("/getMonthlySheduleU?username="+username);
}

async function deleteWorkout(element,event){
    event.preventDefault();
    let id;
    for (const it of element.parentNode.childNodes) {
        if(it.className=='workout_id'){
            id=it.innerHTML;
            break;
        }
    }
    try {
    let response = await fetch("/deleteApi/deleteWorkout?id="+id,{
        method:"DELETE",
          
      });
        var json = await response.json();
    }
       catch (error) {
        console.log(error);
      }
        while(true){
            element = element.parentNode;
            if(element.className=="dayList"){
            break;
            }
        }
    for (const el of element.childNodes ) {
        if( el.className == "username"){
           username=el.innerHTML; 
        }
    }
        console.log(json);
        window.location.replace("/getMonthlySheduleU?username="+username);


}

async function deleteMeal(element,event){
    event.preventDefault();
    let id;
    for (const it of element.parentNode.childNodes) {
        if(it.className=='meal_id'){
            id=it.innerHTML;
            break;
        }
    }
    try {
    let response = await fetch("/deleteApi/deleteMeal?id="+id,{
        method:"DELETE",
          
      });
        var json = await response.json();
    }
       catch (error) {
        console.log(error);
      }
        while(true){
            element = element.parentNode;
            if(element.className=="dayList"){
            break;
            }
        }
    for (const el of element.childNodes ) {
        if( el.className == "username"){
           username=el.innerHTML; 
        }
    }
        console.log(json);
        window.location.replace("/getMonthlySheduleU?username="+username);

}

async function deleteSupplement(element,event){
    event.preventDefault();
    let supple;
    let dayIdI;
    let username;
    for (const it of element.parentNode.childNodes) {
        if(it.className=='supple'){
            supple=it.innerHTML;
            break;
        }
    }
    while(true){
        element = element.parentNode;
        if(element.className=="dayList"){
        break;
        }
    }
    for (const el of element.childNodes ) {
        if( el.className == "dayId"){
            dayIdI=el.innerHTML; 
        }
        if(el.className == "username"){
            username=el.innerHTML;
        }
    }

    try {
    let response = await fetch("/deleteApi/deleteSupplement?dayId="+dayIdI+"&supplement="+supple,{
        method:"DELETE",
          
      });
        var json = await response.json();
    }
       catch (error) {
        console.log(error);
      }
       
        console.log(json);
        window.location.replace("/getMonthlySheduleU?username="+username);

}