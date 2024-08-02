package com.leegosolutions.vms_host_app.database.join;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;

import java.util.List;

public class CS_LoginDetailsWithAccessDetails {

    @Embedded
    public CS_Entity_LoginDetails entity_loginDetails;

    @Embedded
    public CS_Entity_AccessDetails entity_accessDetails;

    public CS_Entity_LoginDetails getEntity_loginDetails() {
        return entity_loginDetails;
    }

    public CS_Entity_AccessDetails getEntity_accessDetails() {
        return entity_accessDetails;
    }
}
