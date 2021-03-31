<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include  "../layout/header.ftl"/>
	<#include  "../layout/common.cssjs.ftl"/>
<style   type="text/css">
  .table th, .table td {
    padding: .2rem;
    vertical-align: middle;
  }
</style>
<script type="text/javascript">
function onClick (event, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("orgsTree");
	nodes = zTree.getCheckedNodes(true);
	var orgsName = "";
	var orgsId = "";
	for (var i=0; i<nodes.length; i++) {
		orgsName += nodes[i].name + ",";
		orgsId += nodes[i].id + ",";
	} 
	
	$("#orgIdsListName").val(orgsName);
	$("#orgIdsList").val(orgsId);
}

$(function () {

	var treeSettings={
		element  :  "orgsTree",
		rootId  :  "1",
	 	checkbox  :  true,
	 	onClick  :  onClick,
	 	onDblClick  :  null,
	 	url  :  "<@base/>/orgs/tree"
	};
		
	function singlePath(newNode) {
		if (newNode === curExpandNode) return;
		if (curExpandNode && curExpandNode.open==true) {
			var zTree = $.fn.zTree.getZTreeObj(treeSettings.element);
			if (newNode.parentTId === curExpandNode.parentTId) {
				zTree.expandNode(curExpandNode, false);
			} else {
				var newParents = [];
				while (newNode) {
					newNode = newNode.getParentNode();
					if (newNode === curExpandNode) {
						newParents = null;
						break;
					} else if (newNode) {
						newParents.push(newNode);
					}
				}
				if (newParents!=null) {
					var oldNode = curExpandNode;
					var oldParents = [];
					while (oldNode) {
						oldNode = oldNode.getParentNode();
						if (oldNode) {
							oldParents.push(oldNode);
						}
					}
					if (newParents.length>0) {
						for (var i = Math.min(newParents.length, oldParents.length)-1; i>=0; i--) {
							if (newParents[i] !== oldParents[i]) {
								zTree.expandNode(oldParents[i], false);
								break;
							}
						}
					} else {
						zTree.expandNode(oldParents[oldParents.length-1], false);
					}
				}
			}
		}
		curExpandNode = newNode;
	};
	
	
	function beforeExpand(treeId, treeNode) {
		var pNode = curExpandNode ? curExpandNode.getParentNode():null;
		var treeNodeP = treeNode.parentTId ? treeNode.getParentNode():null;
		var zTree = $.fn.zTree.getZTreeObj(""+treeSettings.element);
		for(var i=0, l=!treeNodeP ? 0:treeNodeP.children.length; i<l; i++ ) {
			if (treeNode !== treeNodeP.children[i]) {
				zTree.expandNode(treeNodeP.children[i], false);
			}
		}
		while (pNode) {
			if (pNode === treeNode) {
				break;
			}
			pNode = pNode.getParentNode();
		}
		if (!pNode) {
			singlePath(treeNode);
		}
	
	};
	function onLoadSuccessed(){
		var zTree = $.fn.zTree.getZTreeObj("orgsTree");
		var orgsIdValues = $("#orgIdsList").val().split(",") ;
		var orgsName="";
		for (var i=0; i<orgsIdValues.length; i++) {
			var node = zTree.getNodeByParam("id",orgsIdValues[i] );
			if(node != null){
				zTree.checkNode(node, true, false);//将指定ID的节点选中
				orgsName +=  node.name;
			}
		} 
		$("#orgIdsListName").val(orgsName);
	}
	
	$.fn.zTree.init(
		$("#"+treeSettings.element), //element
		{//json object 
			check	: 	{
				enable		: 	treeSettings.checkbox
			},
			async	: 	{
				enable		: 	true,
				url			:	treeSettings.url,
				autoParam	:	["id", "name=n", "level=lv"],
				otherParam	:	{"otherParam":"zTreeAsyncTest",id:treeSettings.rootId},
				dataFilter	: 	function (treeId, parentNode, childNodes) {
									if (!childNodes) return null;
									for (var i=0, l=childNodes.length; i<l; i++) {
										childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
									}
									return childNodes;
								}
			},
			data			: 	{
				simpleData	: 	{
					enable	: 	true
				}
			},
			callback: {
				onClick			: 	treeSettings.onClick,
				onDblClick		: 	treeSettings.onDblClick,
				beforeAsync		: 	function(treeId, treeNode){
					$.loading();
				},
				onAsyncSuccess	: 	function(event, treeId, treeNode, msg){
					$.unloading();
					onLoadSuccessed();
				},
				//beforeExpand	: 	beforeExpand,
				onExpand		: 	function onExpand(event, treeId, treeNode) {
					curExpandNode = treeNode;
				}
			}
		}
	);//end tree
	
});
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "orgIdsListName" || event.target.id == "orgContent" || $(event.target).parents("#orgContent").length>0)) {
		$("#orgContent").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
}
		
function showOrgsTree() {
	var treeObj = $("#orgIdsListName");
	var treeOffset = $("#orgIdsListName").offset();
	$("#orgContent").css({left:treeOffset.left + "px", top:treeOffset.top + treeObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}


</script>
</head>
<body>
<form id="actionForm"  method="post" type="label" autoclose="true"  action="<@base/>/roles/update"  class="needs-validation" novalidate>
	 <table  border="0" cellpadding="0" cellspacing="0" class="table table-bordered">
		<tbody>
		<tr>
			<th><@locale code="role.id" />：</th>
			<td nowrap>
				<input id="id" type="text" readonly name="id"  class="form-control"   value="${model.id}"/>
			</td>
		</tr>
		<tr>
			<th><@locale code="role.name" />：</th>
			<td nowrap>
				<input type="text" id="name" name="name" class="form-control" title="" value="${model.name!}"  required="" />
			</td>
		</tr>
		<tr>
			<th><@locale code="role.dynamic" />：</th>
			<td nowrap>
				<select id="dynamic" name="dynamic"  class="form-control">
					<option value="0" <#if '0'==model.dynamic>selected</#if> ><@locale code="common.text.no" /></option>
					<option value="1" <#if '1'==model.dynamic>selected</#if> ><@locale code="common.text.yes" /></option>
				</select>
			</td>
		</tr>
		<tr>
				<th><@locale code="role.orgidslist" />：</th>
				<td nowrap>
					<input type="text" id="orgIdsListName" name="orgIdsListName"   readonly  class="form-control" title="" value=""   onclick="showOrgsTree();"/>
					<input type="hidden" id="orgIdsList" name="orgIdsList"   readonly  class="form-control" title="" value="${model.orgIdsList!}"   />
				</td>
		</tr>
		<tr>
			<th><@locale code="role.filters" />：</th>
			<td nowrap>
				<textarea id="filters" name="filters" class="form-control"  rows="7" cols="20">${model.filters!}</textarea>
			</td>
		</tr>
		<tr>
                <th><@locale code="common.text.description" />：</th>
                <td nowrap>
                	<textarea id="description" name="description" class="form-control"  rows="6" cols="20">${model.description!}</textarea>
                </td>
            </tr>
		<tr>
			<td nowrap colspan="2"  class="center">
				<input id="_method" type="hidden" name="_method"  value="post"/>
				<input id="status" type="hidden" name="status"  value="1"/>
	    		<input class="button btn btn-primary mr-3"  id="submitBtn" type="submit" value="<@locale code="button.text.save" />">
  				<input class="button btn btn-secondary mr-3"  id="closeBtn"   type="button" value="<@locale code="button.text.cancel" />">	 
			</td>
		</tr>
		</tbody>
	  </table>
</form>
<div id="orgContent" class="menuContent" style="display:none; position: absolute;">
	<ul id="orgsTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
</div>
</body>
</html>