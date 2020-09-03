package apps.developer.fastgrocery.Utils;

import apps.developer.fastgrocery.model.home.Productlist;

public interface ProductItemClickListener {
    void onItemClick(Productlist productItem, int position);
}
