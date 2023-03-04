package com.android.starchat.data.firebase;

import androidx.annotation.NonNull;

import com.android.starchat.data.group.GroupDaoImpl;
import com.android.starchat.data.group.Group;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseDatabase.class, FirebaseStorage.class, DatabaseReference.class})
public class GroupDaoImplTest {
    @Mock
    FirebaseDatabase mockBase;
    @Mock
    FirebaseStorage mockStorage;
    @Mock
    DatabaseReference mockReferenceGroups;
    @Mock
    DatabaseReference mockGroupReference;
    @Mock
    DatabaseReference mockReferenceName;
    @Mock
    DatabaseReference mockReferenceId;
    @Mock
    DatabaseReference mockReferencePhone;
    @Mock
    DatabaseReference mockReferenceLastMessage;
    @Mock
    DatabaseReference mockReferenceDate;
    @Mock
    DatabaseReference mockReferenceTime;

    @Test
    public void TestUpload() {
        String groupId = "testId";
        PowerMockito.mockStatic(FirebaseDatabase.class);
        PowerMockito.mockStatic(FirebaseStorage.class);
        PowerMockito.mockStatic(DatabaseReference.class);
        PowerMockito.when(FirebaseDatabase.getInstance()).thenReturn(mockBase);
        PowerMockito.when(FirebaseStorage.getInstance()).thenReturn(mockStorage);
        PowerMockito.when(mockBase.getReference("groups")).thenReturn(mockReferenceGroups);
        PowerMockito.when(mockReferenceGroups.child(groupId)).thenReturn(mockGroupReference);
        PowerMockito.when(mockGroupReference.child("name")).thenReturn(mockReferenceName);
        PowerMockito.when(mockGroupReference.child("id")).thenReturn(mockReferenceId);
        PowerMockito.when(mockGroupReference.child("phone")).thenReturn(mockReferencePhone);
        PowerMockito.when(mockGroupReference.child("lastMessage")).thenReturn(mockReferenceLastMessage);
        PowerMockito.when(mockGroupReference.child("date")).thenReturn(mockReferenceDate);
        PowerMockito.when(mockGroupReference.child("time")).thenReturn(mockReferenceTime);
        Group group = new Group();
        group.setId(groupId);
        group.setName("testName");
        group.setDate("testDate");
        DatabaseReference databaseReference = mockBase.getReference("groups");
        GroupDaoImpl groupDaoImpl = new GroupDaoImpl(group);
        groupDaoImpl.createGroup();
        databaseReference.child(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("interesting");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}