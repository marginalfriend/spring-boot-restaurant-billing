package io.abun.wmb.Constants;
import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {
    private Integer totalPages;
    private Long    totalElements;
    private Integer page;
    private Integer size;
    private Boolean hasNext;
    private Boolean hasPrevious;
}
