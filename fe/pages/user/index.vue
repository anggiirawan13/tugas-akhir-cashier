<template>
  <v-row class="frame-content">
    <v-col cols="10" offset="1">
      <v-card class="my-3">
        <v-toolbar :color="$vuetify.theme.themes.dark.primary" dark>
          User
          <v-spacer></v-spacer>
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
        </v-toolbar>
        <v-card-text>
          <v-alert v-if="message" :type="alertType">{{ message }}</v-alert>
          <div class="d-flex mb-4">
            <v-breadcrumbs :items="breadcrumbs" class="pa-0" />
            <v-spacer></v-spacer>
            <v-btn dark :color="$vuetify.theme.themes.dark.secondary" to="/user/add" elevation="3" small
              >Add <v-icon right>mdi-plus-circle</v-icon></v-btn
            >
          </div>

          <v-data-table
            :isLoading="isLoading"
            :items="user"
            :headers="headers"
            :items-per-page="10"
            :server-items-length="totalData"
            :options.sync="options"
            :search.sync="search"
            :footer-props="{
              itemsPerPageOptions: [10, 25, 50, 75, 100],
            }"
          >
            <template v-slot:top>
              <v-dialog v-model="dialogDelete" max-width="500px">
                <v-card>
                  <v-card-title
                    >Kamu yakin ingin menghapus data
                    {{ itemDelete.fullname }}?</v-card-title
                  >
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn dark :color="$vuetify.theme.themes.dark.accent" text @click="cancelDelete"
                      >Cancel</v-btn
                    >
                    <v-btn dark :color="$vuetify.theme.themes.dark.secondary"
                      color="error"
                      text
                      @click="confirmDelete(itemDelete.uuid)"
                      >Delete</v-btn
                    >
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </template>
            <template v-slot:item.actions="{ item }">
              <v-btn dark :color="$vuetify.theme.themes.dark.info" :to="`/user/edit/${item.uuid}`" icon
                ><v-icon small>mdi-pencil</v-icon></v-btn
              >
              <v-btn dark :color="$vuetify.theme.themes.dark.warning" small icon @click="deleteItem(item)"
                ><v-icon small>mdi-delete</v-icon></v-btn
              >
            </template>
          </v-data-table>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
export default {
  middleware: ["authenticated"],
  head: {
    title: "User",
  },
  data() {
    return {
      user: [],
      options: {},
      totalData: 0,
      search: "",
      isLoading: false,
      message: "",
      alertType: "",
      dialogDelete: false,
      itemDelete: "",
      headers: [
        { text: "#", value: "number", sortable: false },
        { text: "Fullname", value: "fullname", sortable: false },
        { text: "Email", value: "email", sortable: false },
        { text: "Role", value: "role", sortable: false },
        { text: "Status", value: "status", sortable: false },
        { text: "Actions", value: "actions", sortable: false },
      ],
      breadcrumbs: [
        {
          text: "",
          disabled: true,
          to: "/user",
        },
      ],
    };
  },
  methods: {
    getUser() {
      this.isLoading = true;
      const { page, itemsPerPage } = this.options;

      this.$axios
        .$get(
          `/user?page=${page - 1}&limit=${itemsPerPage}&search=${this.search}`
        )
        .then((response) => {
          if (response.success) {
            this.user = response.data;
            this.totalData = response.additionalEntity.totalData;

            let i = response.additionalEntity.number * itemsPerPage + 1;
            this.user.map((user) => (user.number = i++));
          }
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    confirmDelete(id) {
      this.$axios
        .$delete(`/user/${id}`)
        .then(async () => {
          await this.getUser();
          this.alertType = "success";
          this.message = this.$t("DELETE_SUCCESS", {
            title: this.itemDelete.fullname,
          });
        })
        .catch((error) => {})
        .finally(() => {
          this.cancelDelete();
        });
    },
    deleteItem(item) {
      this.dialogDelete = true;
      this.itemDelete = item;
    },
    cancelDelete() {
      this.dialogDelete = false;
    },
  },
  watch: {
    options: {
      handler() {
        this.getUser();
      },
      deep: true,
    },
    search: {
      handler() {
        this.getUser();
      },
    },
  },
  mounted() {
    if (this.$route.params.message) {
      this.alertType = this.$route.params.type;
      this.message = this.$t(this.$route.params.message, {
        title: this.$route.params.fullname,
      });
    }
  },
};
</script>
