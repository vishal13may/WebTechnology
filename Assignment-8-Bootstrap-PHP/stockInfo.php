<?php
        header("Access-Control-Allow-Origin: *"); 

        if(isset($_GET['chartSymbol'])){
            $symbol = $_GET['chartSymbol'];
            $url = 'http://dev.markitondemand.com/MODApis/Api/v2/InteractiveChart/json?parameters={"Normalized":false,"NumberOfDays":1095,"DataPeriod":"Day","Elements":[{"Symbol":"'.$symbol.'","Type":"price","Params":["ohlc"]}]}';
            $jsonData = file_get_contents($url);
            echo $jsonData;
        }  

        if(isset($_GET['newsFeedsSymbol'])){
            $symbol = $_GET['newsFeedsSymbol'];
            $accountKey = 'MIUIWng73OARxLIJmQM5vIjD9jPfeOj5wphJRLAvwro';
            $context = stream_context_create(array(
                'http' => array(
                'request_fulluri' => true,
                'header'  => "Authorization: Basic " . base64_encode($accountKey . ":" . $accountKey)
                 )
            ));
            $url = 'https://api.datamarket.azure.com/Bing/Search/v1/News?Query=%27'.$symbol.'%27&$format=json';
            $response = file_get_contents($url, 0, $context);
            echo $response;
        } 

        if(isset($_GET['autoCompleteSymbol'])){
            $symbol = $_GET['autoCompleteSymbol'];
            $url = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input=".$symbol;
            $jsonData = file_get_contents($url);
            //print_r($jsonData);
            /*$json = json_decode($jsonData);
            $data = array();
            foreach ($json as $obj){
                $data[]= $obj->{'Symbol'}." - ".$obj->{'Name'}." ( ".$obj->{'Exchange'}." )";
            }*/
            echo json_encode($jsonData);
        } 

        if(isset($_GET['symbol'])){
            $symbol = $_GET['symbol'];
            $url = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol=".$symbol;
            $jsonData = file_get_contents($url);
            echo $jsonData;
        } 
?>
