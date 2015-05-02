package io.katharsis.response;

import io.katharsis.resource.ResourceInformation;
import io.katharsis.resource.registry.RegistryEntry;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.lang.reflect.Field;

public class ResponseContractTest {

    @Test
    public void CollectionResponseEqualsContract() throws NoSuchFieldException {
        EqualsVerifier.forClass(CollectionResponse.class)
                .withPrefabValues(Field.class, String.class.getDeclaredField("value"), String.class.getDeclaredField("hash"))
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void ContainerEqualsContract() throws NoSuchFieldException {
        EqualsVerifier.forClass(Container.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void DataLinksContainerEqualsContract() throws NoSuchFieldException {
        EqualsVerifier.forClass(DataLinksContainer.class)
                .withPrefabValues(Field.class, String.class.getDeclaredField("value"), String.class.getDeclaredField("hash"))
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void LinkageContainerContainerEqualsContract() throws NoSuchFieldException {
        ResourceInformation resourceInformationRed = new ResourceInformation();
        resourceInformationRed.setResourceClass(String.class);
        ResourceInformation resourceInformationBlack = new ResourceInformation();
        resourceInformationBlack.setResourceClass(Integer.class);

        RegistryEntry registryEntryRed = new RegistryEntry(resourceInformationRed, null);
        RegistryEntry registryEntryBlack = new RegistryEntry(resourceInformationBlack, null);

        EqualsVerifier.forClass(LinkageContainer.class)
                .withPrefabValues(Field.class, String.class.getDeclaredField("value"), String.class.getDeclaredField("hash"))
                .withPrefabValues(ResourceInformation.class, resourceInformationRed, resourceInformationBlack)
                .withPrefabValues(RegistryEntry.class, registryEntryRed, registryEntryBlack)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void RelationshipContainerContainerEqualsContract() throws NoSuchFieldException {
        EqualsVerifier.forClass(RelationshipContainer.class)
                .withPrefabValues(Field.class, String.class.getDeclaredField("value"), String.class.getDeclaredField("hash"))
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void ResourceResponseContainerEqualsContract() throws NoSuchFieldException {
        EqualsVerifier.forClass(ResourceResponse.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}