package com.furnaghan.home.component.xbmc.client.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class Permissions {

    @JsonProperty("ReadData")
    private boolean readData;

    @JsonProperty("RemoveData")
    private boolean removeData;
    
    @JsonProperty("UpdateData")
    private boolean updateData;

    @JsonProperty("WriteFile")
    private boolean writeFile;

    @JsonProperty("ControlPVR")
    private boolean controlPVR;

    @JsonProperty("ControlSystem")
    private boolean controlSystem;

    @JsonProperty("ControlPlayback")
    private boolean controlPlayback;

    @JsonProperty("ControlPower")
    private boolean controlPower;

    @JsonProperty("ControlGUI")
    private boolean controlGUI;

    @JsonProperty("ControlNotify")
    private boolean controlNotify;

    @JsonProperty("Navigate")
    private boolean navigate;

    @JsonProperty("ExecuteAddon")
    private boolean executeAddon;

    @JsonProperty("ManageAddon")
    private boolean manageAddon;

    public boolean isReadData() {
        return readData;
    }

    public boolean isRemoveData() {
        return removeData;
    }

    public boolean isUpdateData() {
        return updateData;
    }

    public boolean isWriteFile() {
        return writeFile;
    }

    public boolean isControlPVR() {
        return controlPVR;
    }

    public boolean isControlSystem() {
        return controlSystem;
    }

    public boolean isControlPlayback() {
        return controlPlayback;
    }

    public boolean isControlPower() {
        return controlPower;
    }

    public boolean isControlGUI() {
        return controlGUI;
    }

    public boolean isControlNotify() {
        return controlNotify;
    }

    public boolean isNavigate() {
        return navigate;
    }

    public boolean isExecuteAddon() {
        return executeAddon;
    }

    public boolean isManageAddon() {
        return manageAddon;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("readData", readData)
                .add("removeData", removeData)
                .add("updateData", updateData)
                .add("writeFile", writeFile)
                .add("controlPVR", controlPVR)
                .add("controlSystem", controlSystem)
                .add("controlPlayback", controlPlayback)
                .add("controlPower", controlPower)
                .add("controlGUI", controlGUI)
                .add("controlNotify", controlNotify)
                .add("navigate", navigate)
                .add("executeAddon", executeAddon)
                .add("manageAddon", manageAddon)
                .toString();
    }
}
