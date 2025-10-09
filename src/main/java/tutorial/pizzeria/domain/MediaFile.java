package tutorial.pizzeria.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String filename;

    private String filePath;

    private String contentType;

    private Long size;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
