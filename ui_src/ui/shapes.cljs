(ns ui.shapes)

;; ------------------------------------------------------ ;;
;; ------------------------ SHAPES ---------------------
;; ------------------------------------------------------ ;;

;; beans
(def b1 "M50.0555552,84.5712327 C162.680555,-5.60064227 207.735229,-42.6318923 323.657104,72.3290452 C439.578979,187.289983 416.44618,353.711858 286.086805,318.352483 C155.72743,282.993107 50.0555565,332.133733 50.0555565,332.133733 C50.0555565,332.133733 -62.5694448,174.743108 50.0555552,84.5712327 L50.0555552,84.5712327 Z")
(def b2 "M519.09375,484.882814 C451.984379,569.992191 171.499687,526.703126 121.296563,500.515625 C71.0934394,474.328124 -69.2971885,310.609372 41.5778129,231.585937 C152.452814,152.562503 188.921562,224.437497 267.16375,176.054687 C345.405939,127.671878 334.366874,3.17972922e-06 410.07,4.54747351e-13 C485.773126,-3.17972854e-06 586.203121,399.773438 519.09375,484.882814 L519.09375,484.882814 Z")
(def b3 "M17.9546804,30.1258588 C42.7694777,-2.6260035 51.5623779,0.0529521793 78.3358154,0.0529521793 C105.109253,0.0529521793 131.04553,-2.63044637 157.335815,40.3498272 C209.167302,125.085746 204.119181,108.491012 131.929565,115.54514 C59.7399501,122.599268 39.1561305,145.490452 39.1561305,145.490452 C39.1561305,145.490452 -32.4026937,96.5901449 17.9546804,30.1258588 L17.9546804,30.1258588 Z")
(def b4 "M35.6188634,28.7213441 C85.9762375,-37.742942 261.171725,20.8619855 313.003212,105.597904 C364.834699,190.333822 230.42145,197.804199 158.231835,204.858327 C86.0422195,211.912455 3.04826479,81.1145179 3.04826479,81.1145179 C3.04826479,81.1145179 -14.7385108,95.1856303 35.6188634,28.7213441 L35.6188634,28.7213441 Z")

;; even-point polygons
(def tri "M93.1284047 0.181640625 118.842411 42.5397623 159.704558 109.851086 184.964165 151.460687 90.1132813 151.460687 1.29264466 151.460687 32.4327331 100.164298 67.4644943 42.4572409z")
(def square "M106.691239 1.03515625 155.753248 47.6958994 212.734041 101.887854 164.93318 147.349174 106.691239 202.740551 53.3996618 152.057249 0.6484375 101.887854 54.4994313 50.6725151z")
(def pent "M101.691239 1.03515625 202.543937 70.7226562 184.410137 123.801211 164.021634 183.4794 104.425781 183.4794 39.3608442 183.4794 18.5960963 122.699924 0.838541631 70.7226562z")
(def hex "M92.6912391 1.03515625 184.526999 51.461505 184.526999 101.419922 184.526999 152.314202 92.6912391 202.740551 0.855479021 152.314202 0.855479021 98.7460938 0.855479021 51.461505z")
(def hept "M104.148437 1.03515625 187.056038 39.0072253 207.532525 124.32969 150.158685 192.752994 104.853516 192.752994 58.1381902 192.752994 0.764350366 124.32969 21.2408367 39.0072253z")
(def oct "M107.042802 0.984375 182.026386 30.5234462 213.085603 101.837072 182.026386 173.150699 107.042802 202.68977 32.0592175 173.150699 1 101.837072 32.0592175 30.5234462z")

;; even point lumps 

(def l1 "M215.448924,9.48609165 C301.530697,-15.3512314 354.135609,12.3502985 385.787016,49.118215 C422.235692,91.458888 406.330518,98.2947173 430.897849,145.822529 C451.710633,186.086818 457.132762,181.626719 476.547247,224.702416 C509.901302,298.706395 523.124736,394.172362 348.603682,366.419519 C298.56699,437.010645 253.83434,463.563122 215.965292,466.682259 C132.431036,473.562674 114.064999,402.585729 82.2941663,366.419519 C50.5233338,330.253309 60.9255835,309.139092 48.6410774,276.209357 C36.3565713,243.279622 36.1080978,242.613566 24.0720652,210.349887 C15.4839041,187.328541 0,169.859375 0,145.822529 C6.77795428e-15,41.778694 30.2383309,62.9252251 215.448924,9.48609165 Z")

(def l2 "M323.226563,87.5078125 C508.119785,34.2131867 302.62858,60.006851 461.904468,35.4662705 C612.33165,12.2890625 595.103817,68.4285347 612.33165,136.605079 C627.838136,197.969647 681.857888,463.602204 551.289062,442.859375 C527.757562,476.024373 420.88887,295.556035 400.550036,314.463872 C378.251299,335.193718 357.898829,348.326415 339.55283,355.992157 C260.757602,388.916169 264.992187,190.183594 264.992187,190.183594 C264.992187,190.183594 153.5471,284.971893 94.2194678,236.843818 C69.4318249,216.735456 45.7090789,194.513689 28.2681251,171.922729 C-17.8764476,112.152475 -20.0481957,49.7978465 118.372054,17.1623716 C434.329302,-57.3311709 138.33334,140.802438 323.226563,87.5078125 Z")

(def l3 "M186.925781,68.53125 C232.591032,25.9016494 216.459655,43.1335264 401.249908,5.80953824 C586.040161,-31.51445 589.142369,160.65085 589.142369,160.65085 C589.142369,160.65085 619.363281,283.59375 557.22055,329.525944 C495.077819,375.458139 261.826571,554.761719 261.826571,554.761719 C261.826571,554.761719 267.988608,455.65625 186.925781,455.65625 C105.862954,455.65625 95.4861863,427 95.4861863,427 C95.4861863,427 1,356.662036 1,296.844989 C1,237.027942 26.8813324,221.726741 95.4861863,179.41058 C164.09104,137.09442 118.319871,279.104713 170.773438,289.1875 C223.227004,299.270287 141.260531,111.160851 186.925781,68.53125 Z")

(def l4 "M367.041113,132.970673 C552.234487,79.5288249 350.935774,103.010565 462.654313,32.5559563 C521.450966,-4.5237909 596.069895,65.6092765 613.325695,133.974153 C628.857353,195.508237 597.916276,255.609822 467.135489,234.809692 C443.565788,268.066306 421.572132,293.364201 401.20028,312.32427 C378.865343,333.111381 358.479833,346.280356 340.104052,353.967274 C261.18091,386.982236 219.33051,318.871692 219.33051,318.871692 C219.33051,318.871692 153.796365,282.750821 94.3724214,234.489796 C69.5445389,214.325885 45.7832819,192.042733 28.3140149,169.389366 C-17.9054678,109.454001 -20.0807414,46.9271223 118.564217,14.2014942 C435.034382,-60.4978316 181.847739,186.412522 367.041113,132.970673 Z")

(def l5 "M343.86171,227.154095 C389.52696,184.524494 216.459655,43.1335264 401.249908,5.80953824 C586.040161,-31.51445 589.142369,160.65085 589.142369,160.65085 C589.142369,160.65085 602.57053,265.239552 557.22055,329.525944 C511.870571,393.812337 401.249908,427 401.249908,427 C401.249908,427 342.889398,352.661383 261.826571,352.661383 C180.763744,352.661383 95.4861863,427 95.4861863,427 C95.4861863,427 1,356.662036 1,296.844989 C1,237.027942 26.8813324,221.726741 95.4861863,179.41058 C164.09104,137.09442 209.373004,125.881761 261.826571,135.964549 C314.280138,146.047336 298.196459,269.783695 343.86171,227.154095 Z")

(def l6 "M230.835938,10.234375 C276.501188,-32.3952256 216.459655,84.1335264 401.249908,46.8095382 C586.040161,9.48555004 589.142369,201.65085 589.142369,201.65085 C589.142369,201.65085 602.57053,306.239552 557.22055,370.525944 C511.870571,434.812337 401.249908,468 401.249908,468 C401.249908,468 332.789389,606.039063 251.726562,606.039063 C170.663736,606.039063 95.4861863,468 95.4861863,468 C95.4861863,468 1,397.662036 1,337.844989 C1,278.027942 26.8813324,262.726741 95.4861863,220.41058 C164.09104,178.09442 209.373004,166.881761 261.826571,176.964549 C314.280138,187.047336 185.170687,52.8639756 230.835938,10.234375 Z")


;; UNMORPHABLE LUMPS
(def ul2 "M252.796875,5.75 C338.878648,-19.0873231 509.944226,49.8346628 546.547247,78.546875 C656.859375,165.078125 559.760794,188.503438 584.328125,236.03125 C605.14091,276.295538 666.991765,313.067357 686.40625,356.143054 C719.760305,430.147033 593.124736,535.023858 418.603682,507.271015 C368.56699,577.862141 323.83434,604.414618 285.965292,607.533755 C240.511177,611.277646 293.243488,453.327095 285.965292,452.15625 C279.867831,451.17535 216.544337,616.432652 163.90625,536.5 C137.73806,496.762751 130.925584,449.990587 118.641077,417.060853 C106.356571,384.131118 16.8591927,508.448414 3.171875,399.296875 C-13.46875,266.59375 70,310.710871 70,286.674025 C70,182.63019 67.5862815,59.1891335 252.796875,5.75 Z")

(def ul3 "M343.86171,227.289683 C389.52696,184.634524 216.459655,43.1587871 401.249908,5.81242175 C586.040161,-31.5339437 589.142369,160.746567 589.142369,160.746567 C589.142369,160.746567 602.57053,265.397974 557.22055,329.722909 C511.870571,394.047843 401.249908,427.255404 401.249908,427.255404 L248.368047,477 L95.4861863,427.255404 C95.4861863,427.255404 1,356.87527 1,297.02236 C1,237.16945 26.8813324,221.859076 95.4861863,179.517545 C164.09104,137.176014 209.373004,125.956633 261.826571,136.045465 C314.280138,146.134298 298.196459,269.944842 343.86171,227.289683 Z")