$(document).ready(function() {
    $('.edit-btn').click(function() {
        const userId = $(this).data('id');
        window.location.href = '/edit/' + userId;
    });

    $('.delete-btn').click(function() {
        const userId = $(this).data('id');
        if (confirm('Are you sure you want to delete this user?')) {
            $.ajax({
                url: '/delete/' + userId,
                type: 'DELETE',
                success: function(response) {
                    if (response && response.success) {
                        console.log('Success:', response);
                        alert('User deleted successfully.');
                        $(this).closest('tr').remove();  // Remove the table row dynamically
                    } else {
                        console.log('Failed to delete user:', response);
                        alert('Failed to delete user.');
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error deleting user:', textStatus, errorThrown);
                    alert('Error deleting user.');
                }
            });
        }
    });
});
