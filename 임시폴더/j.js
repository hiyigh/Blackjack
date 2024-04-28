const pwExp = "^[a-zA-Z0-9!@#$%^&*]{4,12}$";
function preventEnter(event){
    event.preventDefault();
}

function resetForm(input) {
    const i_a = document.querySelector("#" + input.id + "_a");
    if (i_a.textContent !== "") {
        i_a.textContent = "";
    }
}
function checkId(input) {
        resetForm(input);
}

function checkPassword(input) {    
    resetForm(input);
    const pw = document.querySelector("#pw");
    const pw_a = document.querySelector("#pw_a");    
    if (pw.value === "") {               
        pw_a.textContent = "";        
        return true;
    }    
    if (!pw.value.match(pwExp)) {        
        pw_a.textContent = "잘못된 입력";
            return false;
    }
    pw_a.textContent = "";
    return true;
}
function passwordCheck(input) {
    resetForm(input);
    const pc = document.querySelector("#pc");
    const pc_a = document.querySelector("#pc_a");
    const pw = document.querySelector("#pw");
    if (pw.value === "") {
        pc_a.textContent = "";
        pc.readOnly = true;
    } else {
        pc.readOnly = false;
        for (let i = 0; i < pw.value.length; ++i) {
            if (pw.value[i] !== pc.value[i] || pw.value.length !== pc.value.length) {                
                pc_a.textContent = "불일치";
                return false;
            }
        }
        pc_a.textContent = "일치";
        return true;
    }
}
function checkName(input) {
    resetForm(input);
    const name = document.querySelector("#name");
    const name_a = document.querySelector("#name_a");
    for (let i = 0; i < name.value.length; ++i) {
        if (name.value[i] === " ") {
            name_a.textContent = "공백";
            return false;
        }
    }
    name_a.textContent = "";
    return true;
}
const phExp = "^(01[016789]{1})[0-9]{3,4}[0-9]{4}$"
function checkPhone(input) {
    resetForm(input)
    let ph = document.querySelector("#phone");
    let ph_a = document.querySelector("#phone_a");
    
    if(ph.value === "") {
        ph_a.textContent = "";    
        return false;    
    } else if(!ph.value.match(phExp)){ 
        ph_a.textContent = "잘못된 형식, 숫자만 입력";
        return false;
    }
    ph_a.textContent = "";
}

const dList = document.querySelector('#domain-list')
const dInput = document.querySelector('#domain-txt')
dList.addEventListener('change', (event) => {
    if(event.target.value !== "type") {
        dInput.value = event.target.value
        dInput.disabled = true
    } else { 
      dInput.value = ""
      dInput.disabled = false
    }
  });

  function checkBlank() {
    let array = document.querySelectorAll(".need");
    for (let i = 0; i < array.length; ++i) {
        let al = document.querySelector("#"+array[i].id + "_a");
        if (array[i].value === "") {
            al.textContent = "필수 입력 사항";
            event.preventDefault();
        } 
    } 
}