<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Assignment 6</title>
    <style type="text/css">
        #output {
            margin-top: 10px;
            width: 900px;
            height: auto;
            margin-left: auto;
            margin-right: auto;
            position: relative;
        }
        
        #output table {
            border-collapse: collapse;
        }
        
        #output td,
        th {
            border: 1px solid #DCDCDC;
            font-size: 20px;
        }
        
        #output table {
            border: 3px solid #DCDCDC;
        }
        
        .searchDiv {
            width: 600px;
            border-color: #DCDCDC;
            border-style: solid;
            padding-left: 4px;
            padding-right: 4px;
            height: 180px;
            margin-left: auto;
            margin-right: auto;
            position: relative;
            background-color: #F3F3F3;
        }
        
        .searchDiv .button {
            border-radius: 6px;
            background-color: #FFFFFF;
            border: 2px solid #DCDCDC;
        }
        
        .searchDiv .txtCompanyName {
            background-color: #FFFFFF;
            border: 2px solid #DCDCDC;
        }
        
        .searchDiv a {
            font-size: 20px;
        }
        
        .searchDiv td {
            font-size: 20px;
        }
        
        .searchDiv hr {
            border-color: #DCDCDC;
            border-style: solid;
            margin-top: 0px;
            margin-bottom: 0px;
        }
        
        .searchDiv h1 {
            padding: 0;
            font-style: italic;
            margin-bottom: 2px;
            margin-top: 3px;
            text-align: center;
        }

    </style>
    <script>
        function clearSearchField() {
            //document.getElementById("formStockSearch").noValidate = true;
            var companyName = document.stockSearchForm.companyName;
            //companyName.style.boxShadow = "none";
            companyName.value = "";
            document.getElementById("output").innerHTML = "";
            //companyName.style.boxShadow = "0 0 3px red";
            //document.getElementById("formStockSearch").noValidate = false;
        }

    </script>
</head>

<body>
    <div class="searchDiv">
        <h1>Stock Search</h1>
        <hr>
        <br>
        <form id="formStockSearch" name="stockSearchForm" action="" method="get">
            <table>
                <tr>
                    <td>Company Name or Symbol:</td>
                    <td>
                        <input type="text" size="40" class="txtCompanyName" name="companyName" value="<?php if(isset($_GET['companyName'])) { echo $_GET['companyName']; }?>" pattern=".*\w+.*" oninvalid="this.setCustomValidity('Please enter company name or symbol')" oninput="setCustomValidity('')" required>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="Submit" class="button" name="submit" value="Search">
                        <input type="Button" value="Clear" class="button" onclick="clearSearchField();">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:center"><a href="http://www.markit.com/product/markit-on-demand" target="_blank">Powerd by Markit on Demand</a> </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="output">
        <?php
        error_reporting(0);
        if(isset($_GET['submit']) && !isset($_GET['symbol'])){
            $companyName = $_GET['companyName'];
            if($companyName == ''){
                echo "<table width=\"100%\" bgcolor=\"#F3F3F3\"><tr><th>Missing compnay name, so no record has been found</th></tr></table>";
            }else{
                    $main_url =$_SERVER['REQUEST_URI'];
                    $url = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/xml?input=".$companyName;
                    $xmlData = simplexml_load_file($url);
                    if(count($xmlData) == 0){
                        echo "<table width=\"100%\" bgcolor=\"#F3F3F3\"><tr><th>No Record has been found</th></tr></table>";    
                    }
                    else if(count($xmlData) == 1 && $xmlData->{"Message"}){
                        echo "<table width=\"100%\" bgcolor=\"#F3F3F3\"><tr><th>Missing compnay name, so no record has been found</th></tr></table>";
                    }else{
                        echo "<table width=\"100%\" bgcolor=\"#F3F3F3\"><tr><th align=\"left\">Name</th><th align=\"left\">Symbol</th><th align=\"left\">Exchange</th><th align=\"left\">Details</th></tr>";
                        foreach($xmlData as $row){
                            echo "<tr>";
                            $symbol;$cmpName;$exchange;
                            foreach($row as $name => $data){
                               if($name == 'Symbol'){
                                    $symbol = $data;
                                }
                                if($name == 'Name'){
                                    $cmpName = $data;
                                }
                                if($name == 'Exchange'){
                                    $exchange = $data;
                                }
                            }
                            echo "<td> $cmpName </td>";
                            echo "<td> $symbol </td>";
                            echo "<td> $exchange </td>";
                            $url = $main_url."&symbol=".$symbol;
                            echo "<td> <a href=\"$url\"> More Details</a> </td>";
                            echo "</tr>";
                        }
                         echo "</tr></table>";
                    }
                    
            }
        }?>

            <?php
            if(isset($_GET['symbol'])){
            $symbol = $_GET['symbol'];
            $url = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol=".$symbol;
            $jsonData = file_get_contents($url);
            //print_r($jsonData);
            $json = json_decode($jsonData);
            /*print_r($json);
            exit;*/
            $tabledef = "<table width=\"100%\" bgcolor=\"#F3F3F3\">";
            $flag=0;
            foreach ($json as $key => $value){
                   if($key == 'Status' && $value!='SUCCESS'){
                       $flag=1;
                       break;
                   }
                    if($key == 'Message'){
                       $flag=1;
                       break;
                   }
                    if($key =='Name'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Name"."</th>";
                        $tabledef = $tabledef."<td align=\"center\">".$value."</td>";
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='Symbol'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Symbol"."</th>";
                        $tabledef = $tabledef."<td align=\"center\">".$value."</td>";
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='LastPrice'){
                        $lastPrice = $value;
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."LastPrice"."</th>";
                        $tabledef = $tabledef."<td align=\"center\">".$value."</td>";
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='Change'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Change"."</th>";
                        $changeValue = trim($value);
                        if(is_numeric($changeValue)){
                            if(round($changeValue,2) > 0.00){
                                $tabledef = $tabledef."<td align=\"center\">".round($changeValue,2)."<img src=\"http://cs-server.usc.edu:45678/hw/hw6/images/Green_Arrow_Up.png\" height=\"15\" width=\"15\">"."</td>";
                            }else if(round($changeValue,2) < 0.00){
                                $tabledef = $tabledef."<td align=\"center\">".round($changeValue,2)."<img src=\"http://cs-server.usc.edu:45678/hw/hw6/images/Red_Arrow_Down.png\" height=\"15\" width=\"15\">"."</td>";
                            }else{
                                $tabledef = $tabledef."<td align=\"center\">".round($changeValue,2)."</td>";
                            }
                        }else{
                            $tabledef = $tabledef."<td align=\"center\">".round($changeValue,2)."</td>"; 
                        }
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='ChangePercent'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Change Percent"."</th>";
                        $changePercent = trim($value);
                        if(is_numeric($changePercent)){
                            if(round($changePercent,2) > 0.00){
                                $tabledef = $tabledef."<td align=\"center\">".round($changePercent,2)."%"."<img src=\"http://cs-server.usc.edu:45678/hw/hw6/images/Green_Arrow_Up.png\" height=\"15\" width=\"15\">"."</td>";
                            }else if(round($changePercent,2) < 0.00){
                                $tabledef = $tabledef."<td align=\"center\">".round($changePercent,2)."%"."<img src=\"http://cs-server.usc.edu:45678/hw/hw6/images/Red_Arrow_Down.png\" height=\"15\" width=\"15\">"."</td>";
                            }else{
                                $tabledef = $tabledef."<td align=\"center\">".round($changePercent,2)."%"."</td>";
                            }
                        }else{
                            $tabledef = $tabledef."<td align=\"center\">".round($changePercent,2)."%"."</td>"; 
                        }
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='Timestamp'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Timestamp"."</th>";
                        $date = new DateTime(trim($value));
                        $date->setTimezone(new DateTimeZone('America/Los_Angeles'));
                        $tabledef = $tabledef."<td align=\"center\">".$date->format("Y-m-d h:i A")." PST</td>";
                        $tabledef = $tabledef."</tr>";
                    }
                   else if($key =='MarketCap'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Market Cap"."</th>";
                        $billion = 1000000000;
                        $million = 1000000 ;
                        if(round(($value/$billion),2) == 0){
                             $tabledef = $tabledef."<td align=\"center\">".round(($value/$million),2)." M"."</td>";
                        }else{
                            $tabledef = $tabledef."<td align=\"center\">".round(($value/$billion),2)." B"."</td>";
                        }
                    }
                    else if($key =='Volume'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Volume"."</th>";
                        $tabledef = $tabledef."<td align=\"center\">".number_format($value)."</td>";
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='ChangeYTD'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Change YTD"."</th>";
                        if(($json->{'LastPrice'} - $value) < 0 ){
                         $tabledef = $tabledef."<td align=\"center\">"."(".round(($json->{'LastPrice'} - $value),2).")"."<img src=\"http://cs-server.usc.edu:45678/hw/hw6/images/Red_Arrow_Down.png\" height=\"15\" width=\"15\">"."</td>";
                        }else if(($json->{'LastPrice'} - $value) > 0){
                            $tabledef = $tabledef."<td align=\"center\">".round(($json->{'LastPrice'} - $value),2)."<img src=\"http://cs-server.usc.edu:45678/hw/hw6/images/Green_Arrow_Up.png\" height=\"15\" width=\"15\">"."</td>"; 
                        }else{
                            $tabledef = $tabledef."<td align=\"center\">".round(($json->{'LastPrice'} - $value),2)."</td>";
                        }

                        $tabledef = $tabledef."</tr>";
                    }
                   else if($key =='ChangePercentYTD'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Change Percent YTD"."</th>";
                        $changePercentYTD = trim($value);
                        if(is_numeric($changePercentYTD)){
                            if(round($changePercentYTD,2) > 0.00){
                                $tabledef = $tabledef."<td align=\"center\">".round($changePercentYTD,2)."%"."<img src=\"http://cs-server.usc.edu:45678/hw/hw6/images/Green_Arrow_Up.png\" height=\"15\" width=\"15\">"."</td>";
                            }else if(round($changePercentYTD,2) < 0.00){
                                $tabledef = $tabledef."<td align=\"center\">".round($changePercentYTD,2)."%"."<img src=\"http://cs-server.usc.edu:45678/hw/hw6/images/Red_Arrow_Down.png\" height=\"15\" width=\"15\">"."</td>";
                            }else{
                                $tabledef = $tabledef."<td align=\"center\">".round($changePercentYTD,2)."</td>";
                            }
                        }else{
                            $tabledef = $tabledef."<td align=\"center\">".round($changePercentYTD,2)."%"."</td>"; 
                        }
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='High'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."High"."</th>";
                        $tabledef = $tabledef."<td align=\"center\">".$value."</td>";
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='Low'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Low"."</th>";
                        $tabledef = $tabledef."<td align=\"center\">".$value."</td>";
                        $tabledef = $tabledef."</tr>";
                    }
                    else if($key =='Open'){
                        $tabledef = $tabledef."<tr>";
                        $tabledef = $tabledef."<th align=\"left\">"."Open"."</th>";
                        $tabledef = $tabledef."<td align=\"center\">".$value."</td>";
                        $tabledef = $tabledef."</tr>";
                    }
            }
            $tabledef = $tabledef."</table>";
            if($flag==1){
                echo "<table width=\"100%\" bgcolor=\"#F3F3F3\"><tr><th>There is no stock information available</th></tr></table>";
            }else{
                $tabledef = $tabledef."</table>";
                echo $tabledef;
            }
        }
         /*if($_SERVER['QUERY_STRING'] != "" ){
            $link=$_SERVER['QUERY_STRING'];
             echo $link;
             echo "Hello...";
         }*/
    ?>
    </div>

</body>

</html>
