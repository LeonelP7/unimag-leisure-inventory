package com.unimag.unimagleisureinventory.model.item;
import com.unimag.unimagleisureinventory.model.enums.ItemCondition;
import com.unimag.unimagleisureinventory.model.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID itemId;
    private String name;
    private String description;
    private int totalQuantity;
    private int availableQuantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_type_id", nullable=false)
    private ItemType itemType;
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;
    @Enumerated(EnumType.STRING)
    private ItemCondition itemCondition;
}
