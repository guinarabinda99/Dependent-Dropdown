<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Dependent Dropdown</title>
</head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<!-- Font icons -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<!-- jQuery -->
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.js"></script>
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- Upload Excel Sheet and Display Bootstrap Table Formate -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.7.7/xlsx.core.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xls/0.7.4-a/xls.core.min.js"></script>

<!-- Data table -->
<link rel="Stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

<body style="background-color: #F0FFFF">
<div class="container" align="center" style="background-color: #FAEBD7; margin-top: 20px;">
  <div class="card" >
  <div class="card-body">
    <form action="saveAll" method="post">
        <h3 class="card-title">Add Country State City</h3>
		<div class="form-group">
			<label for="txtName"> Country</label>
				<span class="colon">:</span> 
				<select name="conId" id="conId" required="required" onchange="findStateByCountry(this.value);">
					<option value="0">--Select--</option>
					<c:forEach items="${cc}" var="con">
						<option value="${con.countryId}">${con.countryName}</option>
					</c:forEach>

				</select>
			
			<div class="clearfix"></div>
		</div>
		
		<div class="form-group">
			<label for="txtName"> State</label>
			
				<span class="colon">:</span> 
				<select name="stId" id="stId" onchange="findCityByState(this.value)" required="required">
					<option value="0">--Select--</option>
					<c:forEach items="${ss}" var="sts">
						<option value="${sts.stateId}">${sts.stateName}</option>
					</c:forEach>

				</select>
			<div class="clearfix"></div>
		</div>
		
		<div class="form-group">
			<label for="txtName"> City</label>
				<span class="colon">:</span> 
				<select name="cityId" id="cityId" required="required">
					<option value="0">--Select--</option>
					<c:forEach items="${dd}" var="dis">
						<option value="${dis.cityId}">${dis.cityName}</option>
					</c:forEach>

				</select>
			<div class="clearfix"></div>
		</div>
        
		<div class="form-group">
			
			<div >
				<input type="submit" class="btn btn-success btn-sm" name="btnSubmit" value="Submit" id="btnSubmit">
				<input type="reset" class="btn btn-danger btn-sm" name="btnReset" value="Reset" id="btnReset">
				<!-- <a href="view">View</a> -->
			</div>
		</div>
	</form>
    
  
  </div>
</div>
<label for="txtName"> Export To Excel:</label>
<a title="Excel" id="anchExcel" runat="server" class="btn btn-inverse btn-sm" onclick="generateExcelsheet()" ><i class="fa fa-file-excel-o"   style="color:greeen; font-size:20px;float:right;"></i></a>

<label for="txtName"> Export To Pdf:</label>
<a title="Pdf" id="anchPdf" runat="server" class="btn btn-inverse btn-sm" onclick="generatePdf()"><i class="fa fa-file-pdf-o" aria-hidden="true" style="color: red; font-size: 20px;" onclick="generatePdf()"></i></a>	
<div id="viewTable" align="center" style="margin-top: 30px">
		<!-- <table class="table table-bordered table-sm table-striped"> -->
		<h3>All Country State City</h3>
		<table border="1" id="myTable" class="table table-striped">
			<thead>
				<tr>
					<th width="40" class="text-center">Sl No.</th>
					<th>Country</th>
					<th>State</th>
					<th>Dist</th>

					<!--  <th>check</th> -->


				</tr>
			</thead>
			<tbody>

				<c:forEach items="${view}" var="vo" varStatus="counter">

					<tr>
						<td class="text-center"><c:out value="${counter.count}" /></td>
						<td>${vo.country.countryName}</td>
						<td>${vo.state.stateName}</td>
						<td>${vo.city.cityName}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="clearfix"></div>
		
							
	</div>
	<form action="country-state-city-excel.htm" id="excelReportForm"  method="POST">
		
	</form>
	<form action="country-state-city-pdf.htm" id="pdfReportForm"  method="POST">
	</form>

</div>


	<%-- ${cc} --%>
</body>
<script type="text/javascript">
function findStateByCountry(countryId) 
{
	 //alert("Dist Id "+districtId);
		
		$.ajax({
			type : "GET",
			url : "find-state-by-country-id.htm", 
		 
			data : {
				"countryId" : countryId,
				
			},
			success : function(response) {
				 //alert(response);
				var html = "<option value=''>---Select---</option>";
				var val=JSON.parse(response);
				 
				if (val != "" || val != null) {
					$.each(val,function(index, value) {						
						html=html+"<option value="+value.stateId+" >"+value.stateName+"</option>";
					});
				}
				$('#stId').empty().append(html);
				$('#cityId').empty();
			},error : function(error) {
				 
			}
		});
	}

function findCityByState(stateId) 
{
	 //alert("Dist Id "+districtId);
	 var countryId=$("#conId").val();
		
		$.ajax({
			type : "GET",
			url : "find-city-by-state-id.htm", 
		 
			data : {
				"stateId" : stateId,
				"countryId":countryId,
				
			},
			success : function(response) {
				 //alert(response);
				var html = "<option value=''>---Select---</option>";
				var val=JSON.parse(response);
				 
				if (val != "" || val != null) {
					$.each(val,function(index, value) {						
						html=html+"<option value="+value.cityId+" >"+value.cityName+"</option>";
					});
				}
				$('#cityId').empty().append(html);
			},error : function(error) {
				 
			}
		});
	}
</script>
<script type="text/javascript">
function generateExcelsheet(){
   	$("#excelReportForm").submit();
    }
function generatePdf(){
   	$("#pdfReportForm").submit();
    }
$(document).ready( function () {
    $('#myTable').DataTable();
} );
</script>
</html>