package io.abun.wmb.ImageHandler;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "images")
@Table(name = "images")
public class ImageEntity {
    @Id
    Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false)

    private String path;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", nullable = false)
    private String contentType;
}
