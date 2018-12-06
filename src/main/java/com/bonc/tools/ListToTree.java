package com.bonc.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListToTree {
	
	/**
	 * 将List<Map>型列表转换为easyui-combotree使用的结构（请测试多实例同时调用的情况，因声明static类型）
	 * 
	 * @param list  带上级编码的数据列表
	 * @param idField  唯一编码字段名
	 * @param textField  显示名称字段名
	 * @param pidField  上级编码字段名
	 * @return  符合easyui-combotree的结构
	 */
	public static List trans(List list, String idField, String textField, String pidField, String stateField, Boolean hasData, List checkedList) {
		TransToTree transToTree = new TransToTree();
		return transToTree.generate(list, idField, textField, pidField, stateField, hasData, checkedList); 
	}
	public static List trans(List list, String idField, String textField, String pidField, String stateField) {
		return trans(list, idField, textField, pidField, stateField, true, null);
	}
	public static List trans(List list, String idField, String textField, String pidField) {
		return trans(list, idField, textField, pidField, null, true, null);
	}
	public static List trans(List list, String idField, String textField, String pidField, List checkedList) {
		return trans(list, idField, textField, pidField, null, true, checkedList);
	}
	

	private static class TransToTree {
		
		private List<Node> root;
		private String idField;
		private String textField;
		private String pidField;
		private String stateField;
		private boolean bAdded;
		
		public List generate(List list, String idField, String textField, String pidField, String stateField, Boolean hasData, List checkedList) {
			
			this.idField = idField;
			this.textField = textField;
			this.pidField = pidField;
			this.stateField = stateField;
			
			sort(list);
			
			for(Map i : (List<Map>)list) {
				
				Object id = i.get(idField);
				Object text = i.get(textField);
				Object pid = i.get(pidField);
				Object state = i.get(stateField);
				Boolean opened = state != null && Long.valueOf(state.toString()) > 0L;
				
				Node node = new Node(id, text, pid, i, isChecked(id, checkedList), opened, hasData);
				
				bAdded = false;
				root = insertRoot(root, node);
			}
			
			return root;
		}
		
		//sort src list
		private void sort(List list) {
			//处理树节点BUF已修复（目前未复现），下面排序不必再处理
			if(1 == 1)
				return;
			
			//这次实现排序是因为递归处理树节点有BUG（没时间调，以后处理，而且在此处排序可能是不稳定排序，影响界面显示次序）
			Collections.sort(list, new Comparator(){
				@Override
				public int compare(Object o1, Object o2) {
					Map m1 = (Map) o1;
					Map m2 = (Map) o2;
					return m1.get(idField).toString().compareTo(m2.get(idField).toString());
				}});
		}
		
		private boolean isChecked(Object tag, List list) {
			if(tag == null || list == null)
				return false;
			
			for(Map i : (List<Map>)list) {
				Object id = i.get(idField);
				if(tag.toString().equals(id.toString())) {
					return true;
				}
			}
			
			return false;
		}
		
		private void appendToChildren(Node treeNode, Node node) {
			if(treeNode.getChildren() == null) {
				treeNode.setChildren(new ArrayList<Node>());
			}
			List list = treeNode.getChildren();
			list.add(node);
		}
		
		//insert into root
		private List<Node> insertRoot(List<Node> list, Node node) {
			
			if(list == null) {
				list = new ArrayList<Node>();
			}
			
			for(Iterator<Node> iter = list.iterator(); iter.hasNext(); ) {
				Node i = iter.next();
				if(i.getId().equals(node.getId())) {
					System.out.println("Error: 出现重复记录，不处理");
					return list;
				} else if(i.getId().equals(node.getPid())) {
					//found node's parent, append
					appendToChildren(i, node);
					bAdded = true;
					return list;
				//} else if(node.getPid() != null && node.getPid().equals(node.getId())) {
				} else if(i.getPid() != null && i.getPid().equals(node.getId())) {
					//found node's children, replace
					appendToChildren(node, i);
					if(!bAdded) {
						list.set(list.indexOf(i), node);
						iter.remove();
					} else {
						System.out.println("ListToTree is error");
					}
				}
				
				if(bAdded == false) {
					i.setChildren(insertRoot(i.getChildren(), node));
				}
			}
			
			if(bAdded == false && (root == null || root == list)) {
				list.add(node);
				bAdded = true;
			}
			
			return list;
		}
		
	}

private static class Node extends HashMap {
	
	public Node(Object id, Object text, Object pid, Object data, Object checked, Boolean opened, Boolean full) {
		setId(id);
		setText(text);
		setPid(pid);
		if(full) setData(data);
		setChecked(checked);
		setState(!opened ? "opened" : "closed");
	}
	
	public Object getId() {
		return get("id");
	}
	public void setId(Object id) {
		put("id", id);
	}
	public Object getText() {
		return get("text");
	}
	public void setText(Object text) {
		put("text", text);
	}
	public Object getPid() {
		return get("pid");
	}
	public void setPid(Object pid) {
		put("pid", pid);
	}
	public Object getData() {
		return get("data");
	}
	public void setData(Object data) {
		put("data", data);
	}
	public List getChildren() {
		return (List)get("children");
	}
	public void setChildren(List children) {
		put("children", children);
	}
	public Object getChecked() {
		return get("checked");
	}
	public void setChecked(Object checked) {
		put("checked", checked);
	}
	public Object getState() {
		return get("state");
	}
	public void setState(Object state) {
		put("state", state);
	}
}

}
