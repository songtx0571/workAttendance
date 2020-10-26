package com.howei.util;

import java.util.List;
import java.util.Objects;

/**
 * 菜单树
 */
public class LayuiTree {
    private Integer id;
    private String title; //父节点id
    private Integer pid;//父id
    private Boolean checked;
    private List<LayuiTree> children; //子节点数据

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LayuiTree> getChildren() {
        return children;
    }

    public void setChildren(List<LayuiTree> children) {
        this.children = children;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LayuiTree layuiTree = (LayuiTree) o;
        return Objects.equals(id, layuiTree.id);
    }
}
