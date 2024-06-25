<template>
    <v-row>
        <v-col cols="10" offset="1">
            <v-card class="mb-2">
                <v-toolbar color="primary" dark>Dashboard</v-toolbar>
                <v-card-text><h2>Hai, {{ fullname }}!</h2></v-card-text>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
import { mapState } from 'vuex';

export default({
    middlewares: ['authenticated'],
    head: {
        title: 'Dashboard'
    },
    computed: {
        ...mapState('auth', {
            fullname: "fullname"
        })
    }
})
</script>