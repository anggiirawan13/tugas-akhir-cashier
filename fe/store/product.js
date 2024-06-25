export const state = () => ({
  categoryId: 0,
  category: [],
  product: [],
  tempProduct: [],
});

export const mutations = {
  updateCategoryId(state, categoryId) {
    state.categoryId = categoryId;
  },
  updateProduct(state, product) {
    state.product = product;
    state.tempProduct = product;
  },
  updateCategory(state, category) {
    if (category) state.category.push(...category);
  },
  searchProduct(state, product) {
    state.product = product;
  },
  resetProduct(state) {
    state.product = state.tempProduct;
  },
};

export const actions = {
  updateCategoryId({ commit }, categoryId) {
    commit("updateCategoryId", categoryId);
  },
  getProduct({ commit }) {
    return this.$axios.$get("/product").then((response) => {
      commit("updateProduct", response.success ? response.result : [""]);
    });
  },
  getCategory({ commit }) {
    return this.$axios.$get("/category").then((response) => {
      commit("updateCategory", response.success ? response.result : [""]);
    });
  },
  searchProduct({ commit }, product) {
    commit("searchProduct", product);
  },
  resetProduct({ commit }) {
    commit("resetProduct");
  },
};
