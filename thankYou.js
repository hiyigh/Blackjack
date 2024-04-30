function call_js() {
    let slideshow = document.querySelector(".slideshow");
    let slidebox = document.querySelector(".slidebox");
    let slideimgs = document.querySelectorAll(".slidebox a");

    let prev = document.querySelector(".prev");
    let next = document.querySelector(".next");
    let indicators = document.querySelectorAll(".indicators a");

    let currentIdx = 0;
    let timer = 0;
    let slidecnt = slideimgs.length;
    for(let i = 0; i < slidecnt; ++i){
        let newleft = i * 100 + '%';
        slideimgs[i].style.left = newleft;
    }

    function gotoSlide(index) {
        currentIdx = index;
        let moveLeft = index * -100 + '%';
        slidebox.style.left = moveLeft;

        indicators.forEach((e) => {
            e.classList.remove("active");
        });
        indicators[currentIdx].classList.add("active");
    }
    gotoSlide(0);

    function startTimer() {
        timer = setInterval(()=>{
            currentIdx += 1;
            let idx = currentIdx % slidecnt;
            gotoSlide(idx);
        },3000);
    }

    startTimer();

    slidebox.addEventListener("mouseenter",function(){
        clearInterval(timer);
    });
    slidebox.addEventListener("mouseleave",function(){
        startTimer();
    });
    prev.addEventListener("mouseenter",function(){
        clearInterval(timer);
    });
    prev.addEventListener("click",function(e){
        e.preventDefault();
        currentIdx -= 1;
        if (currentIdx < 0) {
            currentIdx = slidecnt - 1;
        }
        gotoSlide(currentIdx);
    });
    next.addEventListener("mouseenter",function(){
        clearInterval(timer);
    });
    next.addEventListener("click",function(e){
        e.preventDefault();
        currentIdx += 1;
        if (currentIdx > slidecnt - 1) {
            currentIdx = 0;
        }
        gotoSlide(currentIdx);
    });
    indicators.forEach((e)=>{
        e.addEventListener("mouseenter",()=>{
            clearInterval(timer);
        });
    });
    for (let i = 0; i < indicators.length; ++i) {
        indicators[i].addEventListener("click",(e)=>{
            e.preventDefault();
            gotoSlide(i);
        });
    }
}

function makeBoard(data) {
    let tr = document.querySelector("#board_table_tr");

    // for (let i = 0; i < data.length; ++i) {
    //     let newNum = document.createElement("td");
    //     let newTitle = document.createElement("td");
    //     let newWriter = document.createElement("td");

    //     newNum.textContent = data[i].num;
    //     newTitle.textContent = data[i].title;
    //     newWriter.textContent = data[i].writer;

    //     tr.appendChild(newNum);
    //     tr.appendChild(newTitle);
    //     tr.appendChild(newWriter);

    //     boardTable.appendChild(tr);
    // }
}